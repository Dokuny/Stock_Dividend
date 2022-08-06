package site.notion.dokuny.stock_dividend.model;



import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;
import site.notion.dokuny.stock_dividend.persist.entity.DividendEntity;
import site.notion.dokuny.stock_dividend.utils.CustomLocalDateTimeDeserializer;
import site.notion.dokuny.stock_dividend.utils.CustomLocalDateTimeSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dividend {


//	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime date;
	private String dividend;

	public DividendEntity toEntity(CompanyEntity company) {
		return DividendEntity.builder()
			.company(company)
			.dividend(dividend)
			.date(date)
			.build();
	}


	public static Dividend from(DividendEntity dividendEntity) {
		return Dividend.builder()
			.date(dividendEntity.getDate())
			.dividend(dividendEntity.getDividend())
			.build();
	}
}
