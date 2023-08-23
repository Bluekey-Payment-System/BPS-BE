package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistMonthlyArtistsDto {
    @Schema(description = "멤버 ID", example = "1")
    private Long memberId;

    @Schema(description = "아티스트 한글명", example = "아티스트 1")
    private String name;

    @Schema(description = "아티스트 영어명", example = "artistName1")
    private String enName;

    @Builder
    public ArtistMonthlyArtistsDto(final Long memberId, final String name, final String enName) {
        this.memberId = memberId;
        this.name = name;
        this.enName = enName;
    }
}