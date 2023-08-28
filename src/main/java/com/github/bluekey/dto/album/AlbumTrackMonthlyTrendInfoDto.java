package com.github.bluekey.dto.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 트랙별 월별 매출 정보")
public class AlbumTrackMonthlyTrendInfoDto {
    @Schema(description = "월", example = "7")
    private Integer month;

    @Schema(description = "합", example = "2142344")
    private Double settlement;

    @Schema(description = "매출액", example = "732143")
    private Double revenue;

    @Builder
    public AlbumTrackMonthlyTrendInfoDto(final Integer month, final Double settlement, final Double revenue) {
        this.month = month;
        this.settlement = settlement;
        this.revenue = revenue;
    }
}
