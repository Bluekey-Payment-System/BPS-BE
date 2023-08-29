package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트 월간 트랙 정산 리스트")
public class ArtistMonthlyTrackListDto {

    @Schema(description = "트랙 정보")
    private ArtistMonthlyTrackDto track;

    @Schema(description = "앨범 정보")
    private ArtistMonthlyAlbumDto album;

    @Schema(description = "아티스트 정보 리스트")
    private ArtistMonthlyArtistsDto artists;

    @Schema(description = "매출액", example = "123314")
    private Long revenue;

    @Schema(description = "수익액", example = "1234")
    private Long newIncome;

    @Schema(description = "정산액", example = "1234")
    private Long settlementAmount;

    @Schema(description = "요율", example = "60")
    private Double commissionRate;

    @Builder
    public ArtistMonthlyTrackListDto(final ArtistMonthlyTrackDto track, final ArtistMonthlyAlbumDto album,
            final ArtistMonthlyArtistsDto artists, final Long revenue, final Long newIncome,
            final Long settlementAmount, final Double commissionRate) {
        this.track = track;
        this.album = album;
        this.artists = artists;
        this.revenue = revenue;
        this.newIncome = newIncome;
        this.settlementAmount = settlementAmount;
        this.commissionRate = commissionRate;
    }
}
