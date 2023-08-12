package com.github.bluekey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "로그인 요청")
public class LoginRequestDto {
	@Schema(description = "로그인 ID", example = "bluekey1")
	private final String loginId;
	@Schema(description = "비밀번호", example = "blueblue123")
	private final String password;
}
