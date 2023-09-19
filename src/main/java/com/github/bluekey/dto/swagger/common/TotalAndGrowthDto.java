package com.github.bluekey.dto.swagger.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "총액과 증감률")
public class TotalAndGrowthDto {
	@Schema(description = "총액", example = "1000000")
	private Integer totalAmount;
	@Schema(description = "증감률", example = "2.1")
	private Double growthRate;

	@Builder
	public TotalAndGrowthDto(final Integer totalAmount, final Double growthRate) {
		this.totalAmount = totalAmount;
		this.growthRate = growthRate;
	}
}
