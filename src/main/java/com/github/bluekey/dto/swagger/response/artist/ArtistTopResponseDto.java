package com.github.bluekey.dto.swagger.response.artist;

import com.github.bluekey.dto.swagger.album.AlbumTopDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistTopResponseDto {
    @Schema(description = "앨범의 당월 매출 TOP n 트랙 LIST")
    private List<AlbumTopDto> contents;

    @Builder
    public ArtistTopResponseDto(final List<AlbumTopDto> contents) {
        this.contents = contents;
    }
}
