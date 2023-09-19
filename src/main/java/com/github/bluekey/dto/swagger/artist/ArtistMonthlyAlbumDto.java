package com.github.bluekey.dto.swagger.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistMonthlyAlbumDto {
    @Schema(description = "앨범ID", example = "1")
    private Long albumId;

    @Schema(description = "앨범한글명", example = "앨범1")
    private String name;

    @Schema(description = "앨범영어명", example = "albumName1")
    private String enName;

    @Builder
    public ArtistMonthlyAlbumDto(final Long albumId, final String name, final String enName) {
    	this.albumId = albumId;
    	this.name = name;
    	this.enName = enName;
    }
}
