package com.github.bluekey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistProfileRequestDto {
    @Schema(description = "이메일", example = "example@bluekey_domain.com")
    private final String email;

    @Schema(description = "이미지", example = "https://s3...")
    private final String profileImage;
}
