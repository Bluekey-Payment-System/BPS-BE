package com.github.bluekey.dto.response;

import com.github.bluekey.dto.MonthlyRevenueDto;
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
	private final List<MonthlyRevenueDto> contents;
}
