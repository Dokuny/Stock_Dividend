package site.notion.dokuny.stock_dividend.persist.entity;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity(name= "COMPANY")
public class CompanyEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String ticker;

	@OneToMany(mappedBy = "company",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
	private List<DividendEntity> dividendEntities = new ArrayList<>();

	private String name;
}
