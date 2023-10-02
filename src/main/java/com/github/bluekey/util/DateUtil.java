package com.github.bluekey.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    private static final String MONTH_PREFIX = "01";
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String DATE_FORMAT_WITHOUT_DAY = "yyyyMM";

    private DateUtil() {}

    public static LocalDate convertDateStringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(date + MONTH_PREFIX, formatter);
    }

    public static String getPreviousLocalDateWithOutDay(String monthly) {
        LocalDate date = LocalDate.parse(monthly + MONTH_PREFIX, DateTimeFormatter.ofPattern(DATE_FORMAT));
        LocalDate previousMonth = date.minusMonths(1);
        return previousMonth.format(DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_DAY));
    }

    public static List<Integer> extractMonthsFromDateInformation(String startDate, String endDate) {
        List<Integer> months = new ArrayList<>();

        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int startMonth = Integer.parseInt(startDate.substring(4, 6));
        int endYear = Integer.parseInt(endDate.substring(0, 4));
        int endMonth = Integer.parseInt(endDate.substring(4, 6));

        for (int year = startYear; year <= endYear; year++) {
            int currentStartMonth = (year == startYear) ? startMonth : 1;
            int currentEndMonth = (year == endYear) ? endMonth : 12;

            addMonthsInRange(months, currentStartMonth, currentEndMonth);
        }

        return months;
    }

    private static void addMonthsInRange(List<Integer> months, int start, int end) {
        for (int month = start; month <= end; month++) {
            months.add(month);
        }
    }
}
