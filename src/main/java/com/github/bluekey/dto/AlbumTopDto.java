package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumTopDto {
    @Schema(description = "트랙ID", example = "1")
    private final Long trackId;

    @Schema(description = "트랙한글명", example = "트랙11")
    private final String koTrackName;

    @Schema(description = "트랙영문명", example = "track11")
    private final String eoTrackName;

}
