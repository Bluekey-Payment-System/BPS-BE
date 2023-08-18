package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistTrackTopListDto {
    @Schema(description = "트랙 정보")
    private final List<AlbumTopDto> traack;

    @Schema(description = "매출률", example = "123456789")
    private final long revenue;

    @Schema(description = "이익률", example = "11")
    private final double growthRate;

    @Schema(description = "??", example = "45")
    private final double proportion;
}
