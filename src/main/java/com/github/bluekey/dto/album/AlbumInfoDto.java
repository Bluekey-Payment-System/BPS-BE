package com.github.bluekey.dto.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 정보")
public class AlbumInfoDto {
    @Schema(description = "앨범ID", example = "1")
    private Long albumId;

    @Schema(description = "이미지", example = "https://s3...")
    private String albumImage;

    @Schema(description = "앨범명", example = "앨범명2")
    private String name;

    @Schema(description = "영어앨범명", example = "Album name2")
    private String enName;

    @Builder
    public AlbumInfoDto(final Long albumId, final String albumImage, final String name, final String enName) {
        this.albumId = albumId;
        this.albumImage = albumImage;
        this.name = name;
        this.enName = enName;
    }
}
