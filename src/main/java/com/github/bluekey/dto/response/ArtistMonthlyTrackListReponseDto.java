package com.github.bluekey.dto.response;

import com.github.bluekey.dto.ArtistMonthlyTrackListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistMonthlyTrackListReponseDto {
    @Schema(description = "총 아이템 개수", example = "300")
    private final int totalItems;

    @Schema(description = "앨범 정보")
    private final List<ArtistMonthlyTrackListDto> contents;
}
