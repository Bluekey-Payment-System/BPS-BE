package com.github.bluekey.dto.swagger.response.album;

import com.github.bluekey.dto.swagger.album.AlbumTopDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 당월 매출 TOP n 트랙 LIST")
public class AlbumTopResponseDto {

    @Schema(description = "앨범의 당월 매출 TOP n 트랙 LIST")
    private List<AlbumTopDto> contents;

    @Builder
    private AlbumTopResponseDto(final List<AlbumTopDto> contents) {
        this.contents = contents;
    }

    public static AlbumTopResponseDto from(List<AlbumTopDto> contents) {
        return AlbumTopResponseDto.builder()
                .contents(contents)
                .build();
    }
}
