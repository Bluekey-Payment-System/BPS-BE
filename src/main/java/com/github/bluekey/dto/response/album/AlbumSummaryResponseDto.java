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
	@Schema(description = "앨범ID", example = "1")
	private Long albumId;
	@Schema(description = "앨범명", example = "앨범1")
	private String name;
	@Schema(description = "앨범영어명", example = "albumName1")
	private String enName;
	@Schema(description = "정산금")
	private TotalAndGrowthDto settlement;
	@Schema(description = "베스트트랙")
	private BestTrackDto bestTrack;

	@Builder
	public AlbumSummaryResponseDto(final Long albumId, final String name, final String enName, final TotalAndGrowthDto settlement, final BestTrackDto bestTrack) {
		this.albumId = albumId;
		this.name = name;
		this.enName = enName;
		this.settlement = settlement;
		this.bestTrack = bestTrack;
	}
}
