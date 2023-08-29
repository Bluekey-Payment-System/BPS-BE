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
    private List<ArtistMonthlyArtistsDto> artists;

    @Schema(description = "매출액", example = "123314")
    private Integer revenue;

    @Schema(description = "수익액", example = "1234")
    private Integer netIncome;

    @Schema(description = "정산액", example = "1234")
    private Integer settlementAmount;

    @Schema(description = "요율", example = "60")
    private Integer commissionRate;

    @Builder
    public ArtistMonthlyTrackListDto(final ArtistMonthlyTrackDto track, final ArtistMonthlyAlbumDto album,
            final List<ArtistMonthlyArtistsDto> artists, final Integer revenue, final Integer netIncome,
            final Integer settlementAmount, final Integer commissionRate) {
        this.track = track;
        this.album = album;
        this.artists = artists;
        this.revenue = revenue;
        this.netIncome = netIncome;
        this.settlementAmount = settlementAmount;
        this.commissionRate = commissionRate;
    }
}
