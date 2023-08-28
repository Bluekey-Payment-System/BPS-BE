package com.github.bluekey.dto.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 월별 정산")
public class AlbumMonthlyAccountsDto {
    @Schema(description = "월", example = "7")
    private Integer month;

    @Schema(description = "정산액", example = "2142344")
    private Double settlement;

    @Schema(description = "매출", example = "732143")
    private double revenue;

    @Schema(description = "순수익", example = "141234")
    private Double netIncome;

    @Builder
    public AlbumMonthlyAccountsDto(final Integer month, final Double settlement,
            final double revenue, final Double netIncome) {
        this.month = month;
        this.settlement = settlement;
        this.revenue = revenue;
        this.netIncome = netIncome;
    }
}
