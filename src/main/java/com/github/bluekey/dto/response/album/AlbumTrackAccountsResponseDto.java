package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.album.AlbumTrackAccountsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 트랙별 정산 LIST")
public class AlbumTrackAccountsResponseDto {

    @Schema(description = "앨범의 트랙별 정산 LIST")
    private List<AlbumTrackAccountsDto> tracks;

    @Builder
    public AlbumTrackAccountsResponseDto(final List<AlbumTrackAccountsDto> tracks) {
        this.tracks = tracks;
    }
}
