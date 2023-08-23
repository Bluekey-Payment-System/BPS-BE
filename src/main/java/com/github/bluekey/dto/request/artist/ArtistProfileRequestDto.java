package com.github.bluekey.dto.request.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "아티스트 프로필 수정 요청")
public class ArtistProfileRequestDto {
    @Schema(description = "이메일", example = "example@bluekey_domain.com")
    private String email;

    @Builder
    public ArtistProfileRequestDto(final String email) {
        this.email = email;
    }
}
