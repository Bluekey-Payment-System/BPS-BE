package com.github.bluekey.service.dashboard.v2;

import org.springframework.stereotype.Service;

/**
 * Dashboard의 데이터 연산식 관리
 *
 * @see com.github.bluekey.service.dashboard.DashboardUtilService
 *
 * @author oereo
 * @version 1.0.0
 */
@Service
public class DashboardCalculationService {
    private static final int COMMISSION_TOTAL_INTEGER_THRESHOLD = 100;
    private static final double COMMISSION_TOTAL_DOUBLE_THRESHOLD = 100.0;
    private static final double TAX = 0.033;
    private static final int PERCENTAGE_MIN_THRESHOLD_IN_ONE_DECIMAL_PLACE = 0;
    private static final int PERCENTAGE_MAX_THRESHOLD_IN_ONE_DECIMAL_PLACE = 10;
    private static final int PROPORTION_MIN_THRESHOLD_IN_ONE_DECIMAL_PLACE = 0;
    private static final int PROPORTION_MAX_THRESHOLD_IN_ONE_DECIMAL_PLACE = 1;

    /**
     * 회사 이익 계산 함수
     * 회사 이익 = 매출액 * 사측 요율
     *
     * @param revenue
     * 매출액.
     * @param artistCommissionRate
     * 아티스트 요율.
     *
     * @return 회사 이익 금액
     */
    public int getNetIncome(Double revenue, Integer artistCommissionRate) {
        int companyCommissionRate = COMMISSION_TOTAL_INTEGER_THRESHOLD - artistCommissionRate;
        return (int) Math.floor(revenue * (companyCommissionRate / COMMISSION_TOTAL_DOUBLE_THRESHOLD));
    }

    /**
     * 정산액 계산 함수
     * 정산액 = 매출액 * 요율 - TAX
     *
     * @param revenue
     * 매출액.
     * @param commissionRate
     * 요율.
     *
     * @return 정산액 금액
     */
    public int getSettlement(Double revenue, Integer commissionRate) {
        return (int) Math.floor(revenue * (commissionRate / COMMISSION_TOTAL_DOUBLE_THRESHOLD) * (1 - TAX));
    }

    /**
     * 매출액을 내림하여 정수로 만드는 함수
     *
     * @param revenue
     * 매출액.
     *
     * @return 매출액 금액
     */
    public int getRevenue(Double revenue) {
        return (int) Math.floor(revenue);
    }

    /**
     * 전체 매출액을 100%로 설정했을 때 현재의 매출액이 차지하는 비율 산출 함수
     * (도넛 함수에서 사용)
     *
     * @param amount
     * 매출액.
     *
     * @param totalAmount
     * 전체 매출액.
     *
     * @return 현재의 매출액이 차지하는 비율.
     */
    public double getProportion(double amount, double totalAmount) {
        if (totalAmount == 0.0) {
            return totalAmount;
        }

        double percentage = (amount / totalAmount) * 100;

        if (PROPORTION_MIN_THRESHOLD_IN_ONE_DECIMAL_PLACE < percentage && percentage < PROPORTION_MAX_THRESHOLD_IN_ONE_DECIMAL_PLACE) {
            return Math.floor(percentage * 10) / 10;
        }
        return Math.floor(percentage);
    }

    /**
     * 현재 월의 매출액과 전월의 매출액을 비교하여 비율로 반환하는 함수
     *
     * @param previousMonthAmount
     * 전월 매출액.
     *
     * @param amount
     * 현재 월의 매출액.
     *
     * @return t
     */
    public Double getGrowthRate(Double previousMonthAmount, Double amount) {
        if (isNullOrZeroInGrowthRateCompare(previousMonthAmount, amount)) {
            return null;
        }

        double percentage = (amount - previousMonthAmount) / previousMonthAmount * 100;

        if (PERCENTAGE_MIN_THRESHOLD_IN_ONE_DECIMAL_PLACE < percentage && percentage < PERCENTAGE_MAX_THRESHOLD_IN_ONE_DECIMAL_PLACE) {
            return Math.floor(percentage * PERCENTAGE_MAX_THRESHOLD_IN_ONE_DECIMAL_PLACE) / PERCENTAGE_MAX_THRESHOLD_IN_ONE_DECIMAL_PLACE;
        }
        return Math.floor(percentage);
    }

    private boolean isNullOrZeroInGrowthRateCompare(Double previousMonthAmount, Double amount) {
        if (previousMonthAmount == null) {
            return true;
        }

        if (amount == null) {
            return true;
        }

        if (amount == 0.0) {
            return true;
        }

        if (previousMonthAmount == 0.0) {
            return true;
        }
        return false;
    }

}
