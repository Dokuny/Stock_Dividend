package site.notion.dokuny.stock_dividend.model.constants;

import java.util.List;
import java.util.stream.Collectors;
import site.notion.dokuny.stock_dividend.persist.entity.AuthorityEntity;
import site.notion.dokuny.stock_dividend.persist.entity.MemberEntity;

public enum Authority {
	ROLE_READ,
	ROLE_WRITE;

	public AuthorityEntity toEntity(MemberEntity member) {
		return AuthorityEntity.builder()
			.authority(this)
			.member(member)
			.build();
	}

	public static List<Authority> from(List<AuthorityEntity> entities) {
		return entities.stream().map(entity -> entity.getAuthority()).collect(Collectors.toList());
	}
}
