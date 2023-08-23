package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.album.AlbumTrackAccontsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 트랙별 정산 LIST")
public class AlbumTrackAccontsReponseDto {

    @Schema(description = "앨범의 트랙별 정산 LIST")
    private List<AlbumTrackAccontsDto> tracks;

    @Builder
    public AlbumTrackAccontsReponseDto(final List<AlbumTrackAccontsDto> tracks) {
        this.tracks = tracks;
    }
}
