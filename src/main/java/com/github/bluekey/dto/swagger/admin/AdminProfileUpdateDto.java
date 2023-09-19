package com.github.bluekey.dto.swagger.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "어드민 프로필 수정")
public class AdminProfileUpdateDto {
	@Schema(description = "어드민 닉네임", example = "admin")
	private String nickname;
	@Schema(description = "어드민 이메일", example = "qwerty123@example.com")
	private String email;

	@Builder
	public AdminProfileUpdateDto(final String nickname, final String email) {
		this.nickname = nickname;
		this.email = email;
	}
}
