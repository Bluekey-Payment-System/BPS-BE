package com.github.bluekey.service.member;

import com.github.bluekey.dto.JwtInfoDto;
import com.github.bluekey.dto.LoginMemberDto;
import com.github.bluekey.dto.request.LoginRequestDto;
import com.github.bluekey.dto.response.LoginTokenResponseDto;
import com.github.bluekey.dto.response.LoginTokenResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.jwt.JwtProvider;
import com.github.bluekey.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public LoginTokenResponseDto adminLogin(LoginRequestDto dto) {
		log.info("adminLogin: {}", dto.getLoginId());
		Member member = memberRepository.findByLoginId(dto.getLoginId())
				.orElseThrow(() -> new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED, "아이디 또는 비밀번호가 일치하지 않습니다."));
		if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
			throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED, "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		String token = jwtProvider.generateAccessToken(member.getLoginId(), member.getType(), member.getRole());
		return LoginTokenResponseDto.builder()
				.member(LoginMemberDto.from(member))
				.jwtInformation(JwtInfoDto.builder().accessToken(token).build())
				.build();
	}
}
