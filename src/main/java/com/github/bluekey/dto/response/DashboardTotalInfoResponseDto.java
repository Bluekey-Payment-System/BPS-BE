package com.github.bluekey.dto.response;

import com.github.bluekey.dto.base.artist.BestArtistDto;
import com.github.bluekey.dto.base.TotalAndGrowthDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "대시보드에 보여질 정보")
public class DashboardTotalInfoResponseDto {
	@Schema(description = "총 매출액과 증감률")
	private final TotalAndGrowthDto revenue;
	@Schema(description = "총 회사 이익과 증감률")
	private final TotalAndGrowthDto netIncome;
	@Schema(description = "총 정산액과 증감률")
	private final TotalAndGrowthDto settlementAmount;
	@Schema(description = "월의 Best 아티스트 매출액과 비율")
	private final BestArtistDto bestArtist;
}
