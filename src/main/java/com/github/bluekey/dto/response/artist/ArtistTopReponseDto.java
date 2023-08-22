package com.github.bluekey.dto.response.artist;

import com.github.bluekey.dto.album.AlbumTopDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistTopReponseDto {
    @Schema(description = "앨범의 당월 매출 TOP n 트랙 LIST")
    private final List<AlbumTopDto> contents;
}
