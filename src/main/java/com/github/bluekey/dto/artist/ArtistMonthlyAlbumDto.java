package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistMonthlyAlbumDto {
    @Schema(description = "앨범ID", example = "1")
    private final long albumId;

    @Schema(description = "앨범한글명", example = "앨범1")
    private final String name;

    @Schema(description = "앨범영어명", example = "albumName1")
    private final String enName;
}
