package com.github.bluekey.dto.response.artist;

import com.github.bluekey.dto.artist.ArtistMonthlyInfoDto;
import com.github.bluekey.dto.artist.ArtistMonthlySettlementInfoDto;
import com.github.bluekey.dto.artist.ArtistMonthlyTrackInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트의 당월 정보")
public class ArtistListResponseDto {
    @Schema(description = "당월 정산 정보")
    private ArtistMonthlySettlementInfoDto settlement;

    @Schema(description = "당월 앨범 정보")
    private ArtistMonthlyInfoDto bestAlbum;

    @Schema(description = "당월 트랙 정보")
    private ArtistMonthlyTrackInfoDto bestTrack;

    @Builder
    public ArtistListResponseDto(final ArtistMonthlySettlementInfoDto settlement, final ArtistMonthlyInfoDto bestAlbum, final ArtistMonthlyTrackInfoDto bestTrack) {
        this.settlement = settlement;
        this.bestAlbum = bestAlbum;
        this.bestTrack = bestTrack;
    }

}
