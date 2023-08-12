package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "jwt 정보")
public class JwtInfoDto {
	@Schema(description = "jwt 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJibHVla2V5Iiwia")
	private final String accessToken;
}
