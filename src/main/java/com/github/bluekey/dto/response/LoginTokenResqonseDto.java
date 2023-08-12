package com.github.bluekey.dto.response;

import com.github.bluekey.dto.JwtInfoDto;
import com.github.bluekey.dto.LoginMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class LoginTokenResqonseDto {
	@Schema(description = "로그인 회원 정보")
	private final LoginMemberDto member;
	@Schema(description = "jwt 정보")
	private final JwtInfoDto jwtInformation;
}
