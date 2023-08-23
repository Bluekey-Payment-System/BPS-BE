package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.album.AlbumMonthlyInfoDto;
import com.github.bluekey.dto.album.AlbumMonthlySettlementInfoDto;
import com.github.bluekey.dto.album.AlbumMonthlyTrackInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 리스트 응답")
public class AlbumListResponseDto {
    @Schema(description = "당월 정산 정보")
    private AlbumMonthlySettlementInfoDto settlement;

    @Schema(description = "당월 앨범 정보")
    private AlbumMonthlyInfoDto bestAlbum;

    @Schema(description = "당월 트랙 정보")
    private AlbumMonthlyTrackInfoDto bestTrack;

    @Builder
    public AlbumListResponseDto(final AlbumMonthlySettlementInfoDto settlement, final AlbumMonthlyInfoDto bestAlbum, final AlbumMonthlyTrackInfoDto bestTrack) {
        this.settlement = settlement;
        this.bestAlbum = bestAlbum;
        this.bestTrack = bestTrack;
    }
}
