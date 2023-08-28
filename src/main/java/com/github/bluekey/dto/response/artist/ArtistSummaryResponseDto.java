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
public class ArtistSummaryResponseDto {

    @Schema(description = "당월 정산 정보")
    private ArtistMonthlySettlementInfoDto settlementAmount;

    @Schema(description = "당월 앨범 정보")
    private ArtistMonthlyInfoDto bestAlbum;

    @Schema(description = "당월 트랙 정보")
    private ArtistMonthlyTrackInfoDto bestTrack;

    @Builder
    public ArtistSummaryResponseDto(final ArtistMonthlySettlementInfoDto settlementAmount, final ArtistMonthlyInfoDto bestAlbum, final ArtistMonthlyTrackInfoDto bestTrack) {
        this.settlementAmount = settlementAmount;
        this.bestAlbum = bestAlbum;
        this.bestTrack = bestTrack;
    }

}
