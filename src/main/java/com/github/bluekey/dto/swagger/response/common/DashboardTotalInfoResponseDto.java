package com.github.bluekey.dto.swagger.response.common;

import com.github.bluekey.dto.swagger.artist.BestArtistDto;
import com.github.bluekey.dto.swagger.common.TotalAndGrowthDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "대시보드에 보여질 정보")
public class DashboardTotalInfoResponseDto {
	@Schema(description = "총 매출액과 증감률")
	private TotalAndGrowthDto revenue;
	@Schema(description = "총 회사 이익과 증감률")
	private TotalAndGrowthDto netIncome;
	@Schema(description = "총 정산액과 증감률")
	private TotalAndGrowthDto settlementAmount;
	@Schema(description = "월의 Best 아티스트 매출액과 비율")
	private BestArtistDto bestArtist;

	@Builder
	public DashboardTotalInfoResponseDto(TotalAndGrowthDto revenue, TotalAndGrowthDto netIncome, TotalAndGrowthDto settlementAmount, BestArtistDto bestArtist) {
		this.revenue = revenue;
		this.netIncome = netIncome;
		this.settlementAmount = settlementAmount;
		this.bestArtist = bestArtist;
	}
}
