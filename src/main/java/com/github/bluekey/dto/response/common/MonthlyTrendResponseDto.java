package com.github.bluekey.dto.response.common;

import com.github.bluekey.dto.common.MonthlyTrendDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "월별 매출 추이")
public class MonthlyTrendResponseDto {
	@Schema(description = "월별 매출 정보 리스트")
	private List<MonthlyTrendDto> contents;

	@Builder
	public MonthlyTrendResponseDto(final List<MonthlyTrendDto> contents) {
		this.contents = contents;
	}
}
