package site.notion.dokuny.stock_dividend.scheduler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.notion.dokuny.stock_dividend.model.Company;
import site.notion.dokuny.stock_dividend.model.ScrapedResult;
import site.notion.dokuny.stock_dividend.model.constants.CacheKey;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;
import site.notion.dokuny.stock_dividend.persist.repository.CompanyRepository;
import site.notion.dokuny.stock_dividend.persist.repository.DividendRepository;
import site.notion.dokuny.stock_dividend.scraper.Scraper;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScraperScheduler {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;
	private final Scraper scraper;

	@CacheEvict(value = CacheKey.KEY_FINANCE,allEntries = true)
	@Scheduled(cron = "${scheduler.scrap.yahoo}")
	public void yahooFinanceScheduling() {

		List<CompanyEntity> companies = companyRepository.findAll();

		for (CompanyEntity company : companies) {
			log.info("scraping scheduler is started -> " + company.getName());
			ScrapedResult scrapedResult = scraper.scrap(Company.from(company));

			scrapedResult.getDividends().stream()
				.map(dividend -> dividend.toEntity(company))
				.forEach(dividendEntity -> {
					if (!dividendRepository.existsByCompanyAndDate(dividendEntity.getCompany(),
						dividendEntity.getDate())) {
						dividendRepository.save(dividendEntity);
						log.info(company.getName() + " insert new dividend -> "+ dividendEntity.getDividend());
					}
				});


			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
