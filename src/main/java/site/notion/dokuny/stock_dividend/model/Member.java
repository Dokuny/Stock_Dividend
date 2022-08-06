package site.notion.dokuny.stock_dividend.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.notion.dokuny.stock_dividend.model.constants.Authority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

	private String username;
	private List<Authority> roles;

}
