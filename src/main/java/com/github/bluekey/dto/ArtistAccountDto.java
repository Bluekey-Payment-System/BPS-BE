package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "아티스트 계정 정보")
public class ArtistAccountDto {
	@Schema(description = "아티스트 이름(국문)", example = "혁기")
	private String name;
	@Schema(description = "아티스트 이름(영문)", example = "Hyukki")
	private String enName;
	@Schema(description = "로그인 아이디", example = "qwerty123")
	private String loginId;
	@Schema(description = "이메일", example = "bluekey@gmail.com")
	private String email;
	@Schema(description = "요율", example = "50")
	private BigDecimal commissionRate;

	@Builder
	public ArtistAccountDto(final String name, final String enName,
			final String loginId, final String email, final BigDecimal commissionRate) {
		this.name = name;
		this.enName = enName;
		this.loginId = loginId;
		this.email = email;
		this.commissionRate = commissionRate;
	}
}
