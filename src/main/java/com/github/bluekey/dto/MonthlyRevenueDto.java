package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "월 매출 정보")
public class MonthlyRevenueDto {
	@Schema(description = "월", example = "1")
	private final Long month;
	@Schema(description = "매출", example = "1000000")
	private final Long revenue;
	@Schema(description = "순수익", example = "1000000")
	private final Long netIncome;
}
