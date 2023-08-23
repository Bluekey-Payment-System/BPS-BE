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
    private Long settlement;

    @Schema(description = "매출액", example = "732143")
    private Long revenue;

    @Builder
    public AlbumTrackMonthlyTrendInfoDto(final Integer month, final Long settlement, final Long revenue) {
        this.month = month;
        this.settlement = settlement;
        this.revenue = revenue;
    }
}
