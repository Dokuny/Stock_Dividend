package site.notion.dokuny.stock_dividend.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.notion.dokuny.stock_dividend.model.Auth;
import site.notion.dokuny.stock_dividend.model.Member;
import site.notion.dokuny.stock_dividend.service.MemberService;
import site.notion.dokuny.stock_dividend.security.TokenProvider;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
		Member result = memberService.register(request);
		log.info("user sign up -> " + request.getUsername());
		return ResponseEntity.ok(result);
	}

	@PostMapping("/signin")
	public ResponseEntity<?> singin(@RequestBody Auth.SignIn request) {
		Member member = memberService.authenticate(request);
		String token = tokenProvider.generateToken(member.getUsername(), member.getRoles());
		log.info("user login -> " + request.getUsername());
		return ResponseEntity.ok(token);
	}
}
