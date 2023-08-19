package com.github.bluekey.dto.response;

import com.github.bluekey.dto.base.MonthlyRevenueDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "월별 매출 추이")
public class MonthlyRevenueTrendResponseDto {
	@Schema(description = "월별 매출 정보 리스트")
	private final List<MonthlyRevenueDto> contents;
}
