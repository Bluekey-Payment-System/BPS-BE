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

    @Schema(description = "??", example = "123456789")
    private final Long revenue;

    @Schema(description = "??", example = "11")
    private final int growthRate;

    @Schema(description = "??", example = "45")
    private final int proportion;
}
