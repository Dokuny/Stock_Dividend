package site.notion.dokuny.stock_dividend.persist.entity;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.notion.dokuny.stock_dividend.model.Company;

@Entity(name = "DIVIDEND")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"company_id", "date"})
	}
)
public class DividendEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private CompanyEntity company;

	private LocalDateTime date;

	private String dividend;

}
