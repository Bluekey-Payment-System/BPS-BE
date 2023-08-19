package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "어드민 계정 정보")
public class AdminAccountDto {
	@Schema(description = "어드민 닉네임", example = "admin")
	private String nickname;
	@Schema(description = "로그인 아이디", example = "qwerty123")
	private String loginId;
	@Schema(description = "이메일", example = "bluekey@gmail.com")
	private String email;

	@Builder
	public AdminAccountDto(final String nickname, final String loginId,
			final String email) {
		this.nickname = nickname;
		this.loginId = loginId;
		this.email = email;
	}
}
