package com.github.bluekey.dto.swagger.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumMonthlyInfoDto {
    @Schema(description = "앨범ID", example = "1")
    private final long albumId;

    @Schema(description = "앨범한글명", example = "앨범명2")
    private final String name;

    @Schema(description = "앨범영어명", example = "album2")
    private final String enName;

    @Schema(description = "성장률", example = "2.5")
    private final double growthRate;
}
