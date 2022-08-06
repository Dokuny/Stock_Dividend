package site.notion.dokuny.stock_dividend.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class ScrapedResult {

	private Company company;
	private List<Dividend> dividends;

	public ScrapedResult() {
		this.dividends = new ArrayList<>();
	}

}
