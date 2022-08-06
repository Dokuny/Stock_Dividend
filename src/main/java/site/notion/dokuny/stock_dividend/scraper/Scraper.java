package site.notion.dokuny.stock_dividend.scraper;

import java.util.Optional;
import site.notion.dokuny.stock_dividend.model.Company;
import site.notion.dokuny.stock_dividend.model.ScrapedResult;

public interface Scraper {

	Optional<Company> scrapCompanyByTicker(String ticker);

	ScrapedResult scrap(Company company);

}
