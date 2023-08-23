package com.github.bluekey.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "월 매출 정보")
public class MonthlyRevenueDto {
	@Schema(description = "월", example = "1")
	private Long month;
	@Schema(description = "매출", example = "1000000")
	private Long revenue;
	@Schema(description = "순수익", example = "1000000")
	private Long netIncome;

	@Builder
	public MonthlyRevenueDto(final Long month, final Long revenue, final Long netIncome) {
		this.month = month;
		this.revenue = revenue;
		this.netIncome = netIncome;
	}
}
