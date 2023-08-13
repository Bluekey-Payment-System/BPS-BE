package com.github.bluekey.dto.response;

import com.github.bluekey.dto.AlbumInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistAlbumListReponseDto {
    @Schema(description = "???", example = "300")
    private final int totalItems;

    @Schema(description = "앨범 정보")
    private final List<AlbumInfoDto> contents;
}
