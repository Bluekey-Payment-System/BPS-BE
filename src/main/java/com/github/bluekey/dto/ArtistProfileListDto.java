package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistProfileListDto {
    @Schema(description = "한글 이름", example = "김블루")
    private final String koArtistName;

    @Schema(description = "영문 이름", example = "bluekey")
    private final String enArtisName;

    @Schema(description = "이미지", example = "https://s3...")
    private final String profileImage;
}
