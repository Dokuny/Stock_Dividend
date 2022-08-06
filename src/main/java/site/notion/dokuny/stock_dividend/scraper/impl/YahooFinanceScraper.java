package site.notion.dokuny.stock_dividend.scraper.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import site.notion.dokuny.stock_dividend.exception.impl.FailedScrapException;
import site.notion.dokuny.stock_dividend.model.Company;
import site.notion.dokuny.stock_dividend.model.Dividend;
import site.notion.dokuny.stock_dividend.model.ScrapedResult;
import site.notion.dokuny.stock_dividend.model.constants.Month;
import site.notion.dokuny.stock_dividend.scraper.Scraper;

@Slf4j
@Component
public class YahooFinanceScraper implements Scraper {

	private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
	private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
	private static final long START_TIME = 86400;

	@Override
	public ScrapedResult scrap(Company company) {
		log.info("-- yahooFinance scrap start --");

		ScrapedResult scrapedResult = new ScrapedResult();
		scrapedResult.setCompany(company);

		try {
			long now = System.currentTimeMillis() / 1000;

			String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);

			Document document = Jsoup.connect(url).get();

			Elements parsingDivs = document
				.getElementsByAttributeValue("data-test", "historical-prices");

			Element tableEle = parsingDivs.get(0);

			Element tbody = tableEle.children().get(1);

			List<Dividend> dividends = new ArrayList<>();

			for (Element e : tbody.children()) {
				String txt = e.text();
				if (!txt.endsWith("Dividend")) {
					continue;
				}

				String[] splits = txt.split(" ");
				int month = Month.strToNumber(splits[0]);
				int day = Integer.parseInt(splits[1].replace(",", ""));
				int year = Integer.parseInt(splits[2]);

				String dividend = splits[3];

				if (month < 0) {
					log.warn("Unexpected Month enum value -> {}",splits[0]);
					throw new FailedScrapException();
				}

				dividends.add(Dividend.builder()
					.dividend(dividend)
					.date(LocalDateTime.of(year, month, day, 0, 0))
					.build());

			}
			scrapedResult.setDividends(dividends);

		} catch (IOException e) {
			e.printStackTrace();
			log.warn("scrap failed");
		}

		log.info("-- yahooFinance scrap end --");
		return scrapedResult;
	}

	@Override
	public Optional<Company> scrapCompanyByTicker(String ticker) {
		String url = String.format(SUMMARY_URL, ticker, ticker);

		try {
			Document document = Jsoup.connect(url).get();

			Element titleEle = document.getElementsByTag("h1").get(0);

			String title = titleEle.text();

			String[] s = title.split(" ");

			title = title.replace(s[s.length-1], "").trim();

			return Optional.of(Company.builder()
				.ticker(ticker)
				.name(title)
				.build());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Optional.empty();
	}

}
