package com.github.bluekey.dto.response.artist;

import com.github.bluekey.dto.artist.ArtistMonthlyTrackListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistMonthlyTrackListResponseDto {
    @Schema(description = "총 아이템 개수", example = "300")
    private int totalItems;

    @Schema(description = "앨범 정보")
    private List<ArtistMonthlyTrackListDto> contents;

    @Builder
    public ArtistMonthlyTrackListResponseDto(final int totalItems, final List<ArtistMonthlyTrackListDto> contents) {
        this.totalItems = totalItems;
        this.contents = contents;
    }
}
