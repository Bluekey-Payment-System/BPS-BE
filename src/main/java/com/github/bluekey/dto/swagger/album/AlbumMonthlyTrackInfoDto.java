package com.github.bluekey.dto.swagger.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumMonthlyTrackInfoDto {
    @Schema(description = "트랙ID", example = "1")
    private final long trackId;

    @Schema(description = "앨범한글명", example = "트랙명2")
    private final String name;

    @Schema(description = "앨범영어명", example = "track2")
    private final String enName;

    @Schema(description = "성장률", example = "2.5")
    private final double growthRate;
}
