package com.github.bluekey.dto.response.common;

import com.github.bluekey.dto.common.MonthlyRevenueDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "월별 매출 추이")
public class MonthlyRevenueTrendResponseDto {
	@Schema(description = "월별 매출 정보 리스트")
	private List<MonthlyRevenueDto> contents;

	@Builder
	public MonthlyRevenueTrendResponseDto(final List<MonthlyRevenueDto> contents) {
		this.contents = contents;
	}
}
