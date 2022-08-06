package site.notion.dokuny.stock_dividend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

	private String ticker;
	private String name;

	public CompanyEntity toEntity() {
		return CompanyEntity.builder()
			.ticker(ticker)
			.name(name)
			.build();
	}

	public static Company from(CompanyEntity companyEntity) {
		return Company.builder()
			.ticker(companyEntity.getTicker())
			.name(companyEntity.getName())
			.build();
	}

}
