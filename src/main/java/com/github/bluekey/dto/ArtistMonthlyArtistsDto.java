package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistMonthlyArtistsDto {
    @Schema(description = "아티스트ID", example = "1")
    private final long memberId;

    @Schema(description = "아티스트한글명", example = "혁기")
    private final String koArtistName;

    @Schema(description = "아티스트영어명", example = "hyunki")
    private final String enArtistName;
}
