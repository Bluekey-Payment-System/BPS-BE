package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트의 당월 트랙 정보")
public class ArtistMonthlyTrackInfoDto {
    @Schema(description = "트랙ID", example = "1")
    private Long trackId;

    @Schema(description = "앨범한글명", example = "트랙명2")
    private String name;

    @Schema(description = "앨범영어명", example = "track2")
    private String enName;

    @Schema(description = "성장률", example = "2.5")
    private Double growthRate;

    @Builder
    public ArtistMonthlyTrackInfoDto(final Long trackId, final String name, final String enName, final Double growthRate) {
        this.trackId = trackId;
        this.name = name;
        this.enName = enName;
        this.growthRate = growthRate;
    }
}
