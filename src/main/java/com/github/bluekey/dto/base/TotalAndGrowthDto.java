package com.github.bluekey.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "총액과 증감률")
public class TotalAndGrowthDto {
	@Schema(description = "총액", example = "1000000")
	private final Long totalAmount;
	@Schema(description = "증감률", example = "2.1")
	private final Double growthRate;
}
