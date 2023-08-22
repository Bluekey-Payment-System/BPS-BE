package com.github.bluekey.dto.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumTopDto {
    @Schema(description = "트랙ID", example = "1")
    private final Long trackId;

    @Schema(description = "트랙한글명", example = "트랙11")
    private final String name;

    @Schema(description = "트랙영문명", example = "track11")
    private final String enName;

    @Schema(description = "매출액", example = "1000")
    private final long revenue;

    @Schema(description = "매출액 증감률", example = "9.9")
    private final double growthRate;

    @Schema(description = "매출액 비율", example = "12")
    private final double proportion;

}
