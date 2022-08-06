package site.notion.dokuny.stock_dividend.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import site.notion.dokuny.stock_dividend.exception.impl.NoCompanyException;
import site.notion.dokuny.stock_dividend.model.Company;
import site.notion.dokuny.stock_dividend.model.Dividend;
import site.notion.dokuny.stock_dividend.model.ScrapedResult;
import site.notion.dokuny.stock_dividend.model.constants.CacheKey;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;
import site.notion.dokuny.stock_dividend.persist.entity.DividendEntity;
import site.notion.dokuny.stock_dividend.persist.repository.CompanyRepository;
import site.notion.dokuny.stock_dividend.persist.repository.DividendRepository;

@Service
@RequiredArgsConstructor
public class FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	@Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
	public ScrapedResult getDividendByCompanyName(String companyName) {
		CompanyEntity company = companyRepository.findByName(companyName)
			.orElseThrow(() -> new NoCompanyException());

		List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(
			company.getId());

		return ScrapedResult.builder()
			.company(Company.from(company))
			.dividends(dividendEntities.stream().map(Dividend::from).collect(toList()))
			.build();
	}
}
