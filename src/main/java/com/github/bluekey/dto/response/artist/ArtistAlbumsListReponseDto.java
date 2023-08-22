package com.github.bluekey.dto.response.artist;

import com.github.bluekey.dto.album.AlbumInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistAlbumsListReponseDto {
    @Schema(description = "총 아이템 개수", example = "300")
    private final int totalItems;

    @Schema(description = "앨범 정보")
    private final List<AlbumInfoDto> contents;
}
