package site.notion.dokuny.stock_dividend.persist.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;
import site.notion.dokuny.stock_dividend.persist.entity.DividendEntity;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

	List<DividendEntity> findAllByCompanyId(Long id);

	boolean existsByCompanyAndDate(CompanyEntity companyId, LocalDateTime date);

}
