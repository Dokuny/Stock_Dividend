package site.notion.dokuny.stock_dividend.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.notion.dokuny.stock_dividend.exception.impl.AlreadyExistsCompanyException;
import site.notion.dokuny.stock_dividend.exception.impl.FailedScrapException;
import site.notion.dokuny.stock_dividend.exception.impl.NoCompanyException;
import site.notion.dokuny.stock_dividend.model.Company;
import site.notion.dokuny.stock_dividend.model.ScrapedResult;
import site.notion.dokuny.stock_dividend.model.constants.CacheKey;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;
import site.notion.dokuny.stock_dividend.persist.entity.DividendEntity;
import site.notion.dokuny.stock_dividend.persist.repository.CompanyRepository;
import site.notion.dokuny.stock_dividend.persist.repository.DividendRepository;
import site.notion.dokuny.stock_dividend.scraper.Scraper;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

	private final Scraper scraper;
	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;
	private final CacheManager redisCacheManager;

	public Company save(String ticker) {
		if (companyRepository.existsByTicker(ticker)) {
			throw new AlreadyExistsCompanyException();
		}
		return storeCompanyAndDividend(ticker);
	}
	private Company storeCompanyAndDividend(String ticker) {
		Company company = scraper.scrapCompanyByTicker(ticker)
			.orElseThrow(() -> new FailedScrapException());

		ScrapedResult scrapedResult = scraper.scrap(company);

		CompanyEntity companyEntity = companyRepository.save(company.toEntity());

		List<DividendEntity> dividendEntities = scrapedResult.getDividends().stream()
			.map(dividend -> dividend.toEntity(companyEntity)).collect(toList());

		this.dividendRepository.saveAll(dividendEntities);

		log.info("complete to save company info -> {}", company.getName());
		return company;
	}

	public Page<Company> getAllCompany(final Pageable pageable) {
		return companyRepository.findAllCompany(pageable);
	}

	public List<String> autocomplete(String keyword) {
		return companyRepository.findByNameStartingWithIgnoreCase(keyword,PageRequest.of(0,10))
			.stream()
			.map(company -> company.getName())
			.collect(toList());
	}

	@Transactional
	public Company deleteCompany(String ticker) {
		CompanyEntity company = companyRepository.findByTicker(ticker)
			.orElseThrow(() -> new NoCompanyException());

		clearFinanceCache(company.getName());
		companyRepository.delete(company);

		return Company.builder().name(company.getName()).build();
	}

	private void clearFinanceCache(String companyName) {
		redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
	}

}
