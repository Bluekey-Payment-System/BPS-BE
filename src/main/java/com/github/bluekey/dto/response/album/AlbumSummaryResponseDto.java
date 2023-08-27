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
	@Schema(description = "정산금")
	private TotalAndGrowthDto settlement;
	@Schema(description = "베스트트랙", implementation = BestTrackDto.class)
	private BestTrackDto bestTrack;

	@Builder
	public AlbumSummaryResponseDto(final TotalAndGrowthDto settlement, final BestTrackDto bestTrack) {
		this.settlement = settlement;
		this.bestTrack = bestTrack;
	}
}
