package site.notion.dokuny.stock_dividend.persist.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import site.notion.dokuny.stock_dividend.model.Company;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

	boolean existsByTicker(String ticker);

	Optional<CompanyEntity> findByName(String name);
	Optional<CompanyEntity> findByTicker(String ticker);

	Page<CompanyEntity> findByNameStartingWithIgnoreCase(String s, Pageable pageable);

	@Query(value = "select new site.notion.dokuny.stock_dividend.model.Company(c.ticker,c.name) from COMPANY c",
	countQuery = "select count(c) from COMPANY c")
	Page<Company> findAllCompany(Pageable pageable);
}
