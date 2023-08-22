package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.album.AlbumTrackAccontsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlbumTrackAccontsReponseDto {

    @Schema(description = "앨범의 트랙별 정산 LIST")
    private final List<AlbumTrackAccontsDto> tracks;
}
