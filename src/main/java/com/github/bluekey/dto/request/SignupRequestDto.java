package com.github.bluekey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@Schema(description = "회원가입 요청")
public class SignupRequestDto {
	@Schema(description = "회원 Email", example = "bluekey1@gmail.com")
	private final String email;
	@Schema(description = "회원 id", example = "bluekey1")
	private final String loginId;
	@Schema(description = "회원 닉네임", example = "블루키1")
	private final String nickname;
	@Schema(description = "회원 비밀번호", example = "blueblue123")
	private final String password;
}
