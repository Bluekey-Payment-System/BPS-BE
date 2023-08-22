package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistMonthlyTrackListDto {

    @Schema(description = "트랙 정보")
    private final List<ArtistMonthlyTrackDto> track;

    @Schema(description = "앨범 List 정보")
    private final List<ArtistMonthlyAlbumDto> album;

    @Schema(description = "아티스트 List 정보")
    private final List<ArtistMonthlyArtistsDto> artists;

    @Schema(description = "매출액", example = "123314")
    private final long revenue;

    @Schema(description = "정산액", example = "1234")
    private final long settlementAmount;

    @Schema(description = "요율", example = "60")
    private final double commissionRate;
}
