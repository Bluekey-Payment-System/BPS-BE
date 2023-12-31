package com.github.bluekey.dto.swagger.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "월 매출 + 정산액 or 회사 순이익 정보")
public class MonthlyTrendDto {
	@Schema(description = "월", example = "1")
	private Integer month;
	@Schema(description = "매출", example = "1000000")
	private Integer revenue;
	@Schema(description = "순수익", example = "1000000")
	private Integer netIncome;
	@Schema(description = "정산액", example = "1000000")
	private Integer settlement;

	@Builder
	public MonthlyTrendDto(final Integer month, final Integer revenue, final Integer netIncome,
			final Integer settlement) {
		this.month = month;
		this.revenue = revenue;
		this.netIncome = netIncome;
		this.settlement = settlement;
	}
}
