package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.common.TotalAndGrowthDto;
import com.github.bluekey.dto.track.BestTrackDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범 요약 정보")
public class AlbumSummaryResponseDto {

	@Schema(description = "앨범명", example = "내 마음은")
	private String name;

	@Schema(description = "앨범 영어명", example = "mind")
	private String enName;
	@Schema(description = "총 매출액과 증감률")
	private TotalAndGrowthDto revenue;
	@Schema(description = "총 회사 이익과 증감률")
	private TotalAndGrowthDto newIncome;
	@Schema(description = "총 정산액과 증감률")
	private TotalAndGrowthDto settlementAmount;
	@Schema(description = "베스트 트랙", implementation = BestTrackDto.class)
	private BestTrackDto bestTrack;

	@Builder
	public AlbumSummaryResponseDto(final String name, final String enName, final TotalAndGrowthDto revenue, final TotalAndGrowthDto newIncome, final TotalAndGrowthDto settlementAmount, final BestTrackDto bestTrack) {
		this.name = name;
		this.enName = enName;
		this.revenue = revenue;
		this.newIncome = newIncome;
		this.settlementAmount = settlementAmount;
		this.bestTrack = bestTrack;
	}
}
