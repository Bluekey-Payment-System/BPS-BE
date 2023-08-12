package com.github.bluekey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "비밀번호 변경 요청")
public class PasswordRequestDto {
	@Schema(description = "바꿀 비밀번호", example = "blue123")
	private final String password;
}
