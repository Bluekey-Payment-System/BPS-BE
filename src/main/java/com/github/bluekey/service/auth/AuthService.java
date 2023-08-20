package com.github.bluekey.service.auth;

import com.github.bluekey.dto.JwtInfoDto;
import com.github.bluekey.dto.LoginMemberDto;
import com.github.bluekey.dto.request.LoginRequestDto;
import com.github.bluekey.dto.request.SignupRequestDto;
import com.github.bluekey.dto.response.LoginTokenResponseDto;
import com.github.bluekey.dto.response.LoginTokenResponseDto;
import com.github.bluekey.dto.response.SignupResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.jwt.JwtProvider;
import com.github.bluekey.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public final String AUTHENTICATION_ERROR_MESSAGE = "아이디 또는 비밀번호가 일치하지 않습니다.";

	public LoginTokenResponseDto login(LoginRequestDto dto) {
		Member member = validateLogin(dto);
		String token = jwtProvider.generateAccessToken(member.getLoginId(), member.getType(), member.getRole());
		return generateLoginTokenResponseDto(member, token);
	}

	@Transactional
	public SignupResponseDto createAdmin(SignupRequestDto dto) {
		validateSignUpRequest(dto);
		Member admin = dto.toMember();
		encodeMemberPassword(admin);
		Member newMember = memberRepository.save(admin);
		return SignupResponseDto.from(newMember);
	}

	public String getEncodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private void encodeMemberPassword(Member member) {
		member.updatePassword(getEncodePassword(member.getPassword()));
	}

	private void validateSignUpRequest(SignupRequestDto dto) {
		validateAdminLoginId(dto.getLoginId());
		validateAdminEmail(dto.getEmail());
		validateAdminNickname(dto.getNickname());
	}

	private void validateAdminLoginId(String loginId) {
		memberRepository.findMemberByLoginId(loginId)
				.ifPresent(member -> {throw new BusinessException(ErrorCode.INVALID_LOGIN_ID_VALUE);
				});
	}

	private void validateAdminEmail(String email) {
		memberRepository.findMemberByEmailAndType(email, MemberType.ADMIN)
				.ifPresent(member -> {throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
				});
	}

	private void validateAdminNickname(String nickname) {
		// 아티스트의 활동 예명을 닉네임으로 사용할 수 없다.
		if (memberRepository.findMemberByNameAndType(nickname, MemberType.USER).isPresent() ||
				memberRepository.findMemberByEnNameAndType(nickname, MemberType.USER).isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_NICKNAME_VALUE);
		}
	}

	private Member validateLogin(LoginRequestDto dto){
		Member member = memberRepository.findMemberByLoginId(dto.getLoginId())
				.orElseThrow(() -> new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED, AUTHENTICATION_ERROR_MESSAGE));
		if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
			throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED, AUTHENTICATION_ERROR_MESSAGE);
		}
		return member;
	}

	private LoginTokenResponseDto generateLoginTokenResponseDto(Member member, String token){
		return LoginTokenResponseDto.builder()
				.member(LoginMemberDto.from(member))
				.jwtInformation(JwtInfoDto.builder().accessToken(token).build())
				.build();
	}


}
