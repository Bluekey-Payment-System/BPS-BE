package com.github.bluekey.dto.request.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumsRequestDto {
    @Schema(description = "한글명", example = "사랑")
    private final String name;

    @Schema(description = "영어명", example = "love")
    private final String enName;

    @Schema(description = "고유ID", example = "1")
    private final Long memberId;

    @Schema(description = "영문과 동일 여부", example = "false")
    private final boolean isSameKoNameWithEnName;

    @Schema(description = "이미지", example = "https://s3...")
    private final String albumImage;
}
