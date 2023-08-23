package com.github.bluekey.dto.response.auth;

import com.github.bluekey.dto.auth.JwtInfoDto;
import com.github.bluekey.dto.auth.LoginMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "로그인 토큰 응답")
public class LoginTokenResponseDto {
	@Schema(description = "로그인 회원 정보")
	private LoginMemberDto member;
	@Schema(description = "jwt 정보")
	private JwtInfoDto jwtInformation;

	@Builder
	public LoginTokenResponseDto(final LoginMemberDto member, final JwtInfoDto jwtInformation) {
		this.member = member;
		this.jwtInformation = jwtInformation;
	}
}
