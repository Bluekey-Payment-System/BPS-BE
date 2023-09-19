package com.github.bluekey.dto.swagger.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "로그인 요청")
public class LoginRequestDto {
	@Schema(description = "로그인 ID", example = "bluekey1")
	private String loginId;
	@Schema(description = "비밀번호", example = "blueblue123")
	private String password;

	@Builder
	public LoginRequestDto(String loginId, String password) {
		this.loginId = loginId;
		this.password = password;
	}
}
