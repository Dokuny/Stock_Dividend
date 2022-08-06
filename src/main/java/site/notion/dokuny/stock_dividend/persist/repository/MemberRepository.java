package site.notion.dokuny.stock_dividend.persist.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.notion.dokuny.stock_dividend.persist.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

	@Query("select distinct m from MEMBER m join fetch m.roles where m.username = :username")
	Optional<MemberEntity> findByUsernameWithRoles(@Param("username") String username);

	boolean existsByUsername(String username);
}
