package site.notion.dokuny.stock_dividend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import site.notion.dokuny.stock_dividend.model.constants.Authority;
import site.notion.dokuny.stock_dividend.service.MemberService;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	@Value("${spring.jwt.secret}")
	private String secretKey;

	private Key encodedSecretKey;

	private static final String KEY_ROLES = "roles";
	private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;

	private final MemberService memberService;

	@PostConstruct
	protected void init() {
		encodedSecretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));

	}

	public String generateToken(String username, List<Authority> roles) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put(KEY_ROLES, roles);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expiredDate)
			.signWith(encodedSecretKey)
			.compact();
	}

	public String getUsername(String token) {
		return parseClaims(token).getSubject();
	}

	public boolean validateToken(String token) {
		if(!StringUtils.hasText(token)) return false;
		Claims claims = parseClaims(token);
		return !claims.getExpiration().before(new Date());
	}

	private Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(encodedSecretKey)
				.build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public Authentication getAuthentication(String jwt) {
		UserDetails userDetails = memberService.loadUserByUsername(getUsername(jwt));
		return new UsernamePasswordAuthenticationToken(userDetails, "",
			userDetails.getAuthorities());
	}
}
