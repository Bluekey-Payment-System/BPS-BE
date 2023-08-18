package com.github.bluekey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminArtistProfileRequestDto {
    @Schema(description = "아티스트한글명", example = "혁기")
    private final String koArtistName;

    @Schema(description = "아티스트영어명", example = "hyunki")
    private final String enArtistName;

    @Schema(description = "아티스트기본요율", example = "40")
    private final String commissionRate;
}
