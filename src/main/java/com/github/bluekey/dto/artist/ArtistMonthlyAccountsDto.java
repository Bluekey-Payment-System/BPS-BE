package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Schema(description = "아티스트의 월별 정산 정보")
public class ArtistMonthlyAccountsDto {
    @Schema(description = "월", example = "7")
    private Integer month;

    @Schema(description = "정산액", example = "2142344")
    private Double settlement;

    @Schema(description = "매출", example = "732143")
    private double revenue;

    @Schema(description = "회사 순이익", example = "732143")
    private Double netIncome;

    @Builder
    public ArtistMonthlyAccountsDto(final Integer month, final Double settlement,
            final double revenue, final Double netIncome) {
        this.month = month;
        this.settlement = settlement;
        this.revenue = revenue;
        this.netIncome = netIncome;
    }
}
