package com.github.bluekey.dto.response;

import com.github.bluekey.dto.base.album.AlbumMonthlyInfoDto;
import com.github.bluekey.dto.base.album.AlbumMonthlySettlementInfoDto;
import com.github.bluekey.dto.base.album.AlbumMonthlyTrackInfoDto;
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
