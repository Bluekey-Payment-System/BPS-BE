package com.github.bluekey.dto.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumInfoDto {
    @Schema(description = "앨범ID", example = "1")
    private final Long albumId;

    @Schema(description = "이미지", example = "https://s3...")
    private final String albumImage;

    @Schema(description = "앨범명", example = "앨범명2")
    private final String koName;
}
