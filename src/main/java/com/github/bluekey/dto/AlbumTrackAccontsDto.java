package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlbumTrackAccontsDto {
    @Schema(description = "트랙ID", example = "1")
    private final int trackId;

    @Schema(description = "한글트랙명", example = "곡제목111")
    private final int name;

    @Schema(description = "영문트랙명", example = "track111")
    private final int enName;

    @Schema(description = "월별 트랙 정산 정보")
    private final List<AlbumTrackMonthlyTrendInfoDto> monthlyTrend;
}
