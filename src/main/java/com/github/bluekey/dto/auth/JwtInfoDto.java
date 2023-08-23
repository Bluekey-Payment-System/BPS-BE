package com.github.bluekey.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(description = "jwt 정보")
public class JwtInfoDto {
	@Schema(description = "jwt 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJibHVla2V5Iiwia")
	private String accessToken;

	public JwtInfoDto(final String accessToken) {
		this.accessToken = accessToken;
	}
}
