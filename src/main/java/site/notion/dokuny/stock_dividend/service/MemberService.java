package site.notion.dokuny.stock_dividend.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.notion.dokuny.stock_dividend.exception.impl.AlreadyExistUserException;
import site.notion.dokuny.stock_dividend.exception.impl.NoUserException;
import site.notion.dokuny.stock_dividend.exception.impl.NotMatchedPasswordException;
import site.notion.dokuny.stock_dividend.model.Auth;
import site.notion.dokuny.stock_dividend.model.Member;
import site.notion.dokuny.stock_dividend.model.constants.Authority;
import site.notion.dokuny.stock_dividend.persist.entity.MemberEntity;
import site.notion.dokuny.stock_dividend.persist.repository.AuthorityRepository;
import site.notion.dokuny.stock_dividend.persist.repository.MemberRepository;
import site.notion.dokuny.stock_dividend.security.TokenProvider;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final AuthorityRepository authorityRepository;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByUsernameWithRoles(username)
			.orElseThrow(() -> new NoUserException());
	}

	@Transactional
	public Member register(Auth.SignUp member) {
		if (memberRepository.existsByUsername(member.getUsername())) {
			throw new AlreadyExistUserException();
		}

		member.setPassword(passwordEncoder.encode(member.getPassword()));

		MemberEntity memberEntity = memberRepository.save(member.toEntity());

		List<Authority> roles = member.getRoles();

		for (Authority role : roles) {
			authorityRepository.save(role.toEntity(memberEntity));
		}

		return Member.builder()
			.username(memberEntity.getUsername())
			.roles(roles)
			.build();
	}

	public Member authenticate(Auth.SignIn member) {
		MemberEntity memberEntity = memberRepository.findByUsernameWithRoles(member.getUsername())
			.orElseThrow(
				() -> new NoUserException());

		if (!passwordEncoder.matches(member.getPassword(), memberEntity.getPassword())) {
			throw new NotMatchedPasswordException();
		}

		return Member.builder()
			.username(memberEntity.getUsername())
			.roles(Authority.from(memberEntity.getRoles()))
			.build();
	}

}
