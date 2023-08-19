package com.github.bluekey.dto.response;

import com.github.bluekey.dto.JwtInfoDto;
import com.github.bluekey.dto.LoginMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginTokenResponseDto {
	@Schema(description = "로그인 회원 정보")
	private final LoginMemberDto member;
	@Schema(description = "jwt 정보")
	private final JwtInfoDto jwtInformation;

	@Builder
	public LoginTokenResponseDto(final LoginMemberDto member, final JwtInfoDto jwtInformation) {
		this.member = member;
		this.jwtInformation = jwtInformation;
	}
}
