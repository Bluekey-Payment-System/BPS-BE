package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.album.AlbumTrackTrendDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 트랙별 정산 LIST")
public class AlbumTrackTrendResponseDto {

    @Schema(description = "앨범의 트랙별 정산 LIST")
    private List<AlbumTrackTrendDto> tracks;

    @Builder
    public AlbumTrackTrendResponseDto(final List<AlbumTrackTrendDto> tracks) {
        this.tracks = tracks;
    }
}
