package com.github.bluekey.dto.request.auth;

import com.github.bluekey.validator.annotations.PasswordValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "비밀번호 변경 요청")
public class PasswordRequestDto {
	@PasswordValidation
	@Schema(description = "바꿀 비밀번호", example = "blue123")
	private String password;

	@Builder
	private PasswordRequestDto(final String password) {
		this.password = password;
	}
}
