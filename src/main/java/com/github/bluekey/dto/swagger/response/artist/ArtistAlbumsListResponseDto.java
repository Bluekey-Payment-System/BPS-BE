package com.github.bluekey.dto.swagger.response.artist;

import com.github.bluekey.dto.swagger.album.AlbumInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트의 앨범 리스트 응답")
public class ArtistAlbumsListResponseDto {
    @Schema(description = "총 아이템 개수", example = "300")
    private Long totalItems;

    @Schema(description = "앨범 정보")
    private List<AlbumInfoDto> contents;

    @Builder
    public ArtistAlbumsListResponseDto(final Long totalItems, final List<AlbumInfoDto> contents) {
        this.totalItems = totalItems;
        this.contents = contents;
    }
}
