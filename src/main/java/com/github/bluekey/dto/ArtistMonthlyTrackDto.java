package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistMonthlyTrackDto {
    @Schema(description = "트랙ID", example = "1")
    private final long trackId;
    @Schema(description = "트랙한글명", example = "곡명1")
    private final String koTrackName;
    @Schema(description = "트랙영어명", example = "trackName1")
    private final String enTrackName;
}
