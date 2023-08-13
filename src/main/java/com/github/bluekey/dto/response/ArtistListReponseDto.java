package com.github.bluekey.dto.response;

import com.github.bluekey.dto.AlbumMonthlyInfoDto;
import com.github.bluekey.dto.AlbumMonthlySettlementInfoDto;
import com.github.bluekey.dto.AlbumMonthlyTrackInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistListReponseDto {
    @Schema(description = "당월 정산 정보")
    private final List<AlbumMonthlySettlementInfoDto> settlement;

    @Schema(description = "당월 앨범 정보")
    private final List<AlbumMonthlyInfoDto> bestAlbum;

    @Schema(description = "당월 트랙 정보")
    private final List<AlbumMonthlyTrackInfoDto> bestTrack;
}
