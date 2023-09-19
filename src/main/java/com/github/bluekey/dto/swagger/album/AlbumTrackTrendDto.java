package com.github.bluekey.dto.swagger.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 트랙 월별 정산 LIST")
public class AlbumTrackTrendDto {
    @Schema(description = "트랙ID", example = "1")
    private Long trackId;

    @Schema(description = "한글트랙명", example = "곡제목111")
    private String name;

    @Schema(description = "영문트랙명", example = "track111")
    private String enName;

    @Schema(description = "월별 트랙 정산 정보")
    private List<AlbumTrackMonthlyTrendInfoDto> monthlyTrend;

    @Builder
    public AlbumTrackTrendDto(final Long trackId, final String name, final String enName, final List<AlbumTrackMonthlyTrendInfoDto> monthlyTrend) {
        this.trackId = trackId;
        this.name = name;
        this.enName = enName;
        this.monthlyTrend = monthlyTrend;
    }
}
