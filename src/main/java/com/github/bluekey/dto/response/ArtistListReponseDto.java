package com.github.bluekey.dto.response;

import com.github.bluekey.dto.base.artist.ArtistMonthlyInfoDto;
import com.github.bluekey.dto.base.artist.ArtistMonthlySettlementInfoDto;
import com.github.bluekey.dto.base.artist.ArtistMonthlyTrackInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistListReponseDto {
    @Schema(description = "당월 정산 정보")
    private final ArtistMonthlySettlementInfoDto settlement;

    @Schema(description = "당월 앨범 정보")
    private final ArtistMonthlyInfoDto bestAlbum;

    @Schema(description = "당월 트랙 정보")
    private final ArtistMonthlyTrackInfoDto bestTrack;
}
