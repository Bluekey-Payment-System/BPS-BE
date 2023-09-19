package com.github.bluekey.dto.swagger.filter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyTrackFilter {
    private Long memberId;
    private Integer revenueFrom;
    private Integer revenueTo;
    private Integer netIncomeFrom;
    private Integer netIncomeTo;
    private Integer settlementFrom;
    private Integer settlementTo;
    private Integer commissionRateFrom;
    private Integer commissionRateTo;

    @Builder
    public MonthlyTrackFilter(Long memberId, Integer revenueFrom, Integer revenueTo, Integer netIncomeFrom, Integer netIncomeTo, Integer settlementFrom, Integer settlementTo, Integer commissionRateFrom, Integer commissionRateTo) {
        this.memberId = memberId;
        this.revenueFrom = revenueFrom;
        this.revenueTo = revenueTo;
        this.netIncomeFrom = netIncomeFrom;
        this.netIncomeTo = netIncomeTo;
        this.settlementFrom = settlementFrom;
        this.settlementTo = settlementTo;
        this.commissionRateFrom = commissionRateFrom;
        this.commissionRateTo = commissionRateTo;
        convertNullToValue();
    }

    private void convertNullToValue() {
        if (commissionRateFrom == null) {
            this.commissionRateFrom = 0;
        }

        if (commissionRateTo == null) {
            this.commissionRateTo = 100;
        }
    }
}
