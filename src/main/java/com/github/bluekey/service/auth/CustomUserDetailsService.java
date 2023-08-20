package com.github.bluekey.service.auth;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) {
		Member member = memberRepository.findMemberByLoginId(loginId)
				.orElseThrow(()-> new UsernameNotFoundException("Member not found"));

		return User.withUsername(member.getId().toString())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
}
