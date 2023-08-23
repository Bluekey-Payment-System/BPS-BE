package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistMonthlyTrackDto {
    @Schema(description = "트랙ID", example = "1")
    private Long trackId;

    @Schema(description = "트랙한글명", example = "곡명1")
    private String name;

    @Schema(description = "트랙영어명", example = "trackName1")
    private String enName;

    @Builder
    public ArtistMonthlyTrackDto(Long trackId, String name, String enName) {
    	this.trackId = trackId;
    	this.name = name;
    	this.enName = enName;
    }
}
