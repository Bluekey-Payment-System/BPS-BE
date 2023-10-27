package com.github.bluekey.service.auth;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) {
		Member member = memberRepository.findMemberByLoginIdAndIsRemoved(loginId, false)
				.orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		return User.withUsername(member.getId().toString())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
}
