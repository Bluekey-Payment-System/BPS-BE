package com.github.bluekey.dto.swagger.response.artist;

import com.github.bluekey.dto.swagger.artist.ArtistMonthlyInfoDto;
import com.github.bluekey.dto.swagger.artist.ArtistMonthlySettlementInfoDto;
import com.github.bluekey.dto.swagger.artist.ArtistMonthlyTrackInfoDto;
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

    @Schema(description = "아티스트 PK", example = "1")
    private Long memberId;
    @Schema(description = "아티스트 명", example = "혁기")
    private String name;
    @Schema(description = "아티스트 영어명", example = "hucki")
    private String enName;
    @Schema(description = "당월 정산 정보")
    private ArtistMonthlySettlementInfoDto settlementAmount;

    @Schema(description = "당월 앨범 정보")
    private ArtistMonthlyInfoDto bestAlbum;

    @Schema(description = "당월 트랙 정보")
    private ArtistMonthlyTrackInfoDto bestTrack;

    @Builder
    public ArtistSummaryResponseDto(
            final Long memberId,
            final String name,
            final String enName,
            final ArtistMonthlySettlementInfoDto settlementAmount,
            final ArtistMonthlyInfoDto bestAlbum,
            final ArtistMonthlyTrackInfoDto bestTrack) {
        this.memberId = memberId;
        this.name = name;
        this.enName = enName;
        this.settlementAmount = settlementAmount;
        this.bestAlbum = bestAlbum;
        this.bestTrack = bestTrack;
    }

}
