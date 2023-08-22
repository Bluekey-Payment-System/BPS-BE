package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.album.AlbumMonthlyInfoDto;
import com.github.bluekey.dto.album.AlbumMonthlySettlementInfoDto;
import com.github.bluekey.dto.album.AlbumMonthlyTrackInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumListReponseDto {
    @Schema(description = "당월 정산 정보")
    private final AlbumMonthlySettlementInfoDto settlement;

    @Schema(description = "당월 앨범 정보")
    private final AlbumMonthlyInfoDto bestAlbum;

    @Schema(description = "당월 트랙 정보")
    private final AlbumMonthlyTrackInfoDto bestTrack;

}
