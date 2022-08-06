package site.notion.dokuny.stock_dividend.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.notion.dokuny.stock_dividend.persist.entity.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity,Long> {

}
