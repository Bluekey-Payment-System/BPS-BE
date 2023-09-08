package com.github.bluekey.dto.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트의 임시 비밀번호 발급 응답")
public class NewPasswordResponseDto {
	@Schema(description = "새로운 비밀번호", example = "qwerty123")
	private String newPassword;

	@Builder
	public NewPasswordResponseDto(final String newPassword) {
		this.newPassword = newPassword;
	}
}
