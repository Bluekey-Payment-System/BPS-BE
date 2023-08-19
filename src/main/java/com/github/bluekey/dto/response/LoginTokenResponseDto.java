package com.github.bluekey.dto.response;

import com.github.bluekey.dto.JwtInfoDto;
import com.github.bluekey.dto.LoginMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class LoginTokenResponseDto {
	@Schema(description = "로그인 회원 정보")
	private final LoginMemberDto member;
	@Schema(description = "jwt 정보")
	private final JwtInfoDto jwtInformation;

	@Builder
	public LoginTokenResponseDto(LoginMemberDto member, JwtInfoDto jwtInformation) {
		this.member = member;
		this.jwtInformation = jwtInformation;
	}
}
