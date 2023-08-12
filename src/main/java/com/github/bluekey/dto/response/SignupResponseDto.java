package com.github.bluekey.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "회원가입 응답")
public class SignupResponseDto {
	@Schema(description = "회원 고유 id", example = "1")
	private final Long id;
	@Schema(description = "회원 Email", example = "bluekey1@gmail.com")
	private final String email;
	@Schema(description = "회원 이름", example = "bluekey1")
	private final String loginId;
	@Schema(description = "회원 닉네임", example = "블루키1")
	private final String nickname;
	@Schema(description = "회원 비밀번호", example = "blueblue123")
	private final String password;
}
