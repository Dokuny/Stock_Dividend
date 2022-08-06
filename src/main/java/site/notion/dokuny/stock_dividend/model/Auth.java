package site.notion.dokuny.stock_dividend.model;

import java.util.List;
import lombok.Data;
import site.notion.dokuny.stock_dividend.model.constants.Authority;
import site.notion.dokuny.stock_dividend.persist.entity.MemberEntity;

public class Auth {

	@Data
	public static class SignIn{

		private String username;
		private String password;
	}

	@Data
	public static class SignUp{

		private String username;
		private String password;
		private List<Authority> roles;

		public MemberEntity toEntity() {
			return MemberEntity.builder()
				.username(username)
				.password(password)
				.build();
		}

	}
}
