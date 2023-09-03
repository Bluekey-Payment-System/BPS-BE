package com.github.bluekey.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class DashboardUtil {
	private static final String MONTH_PREFIX = "01";

	public Double getCompanyNetIncome(Double revenue, Integer artistCommissionRate) {
		int companyCommissionRate = 100 - artistCommissionRate;
		return Math.floor(revenue * (companyCommissionRate / 100.0));
	}

	public Double getArtistSettlement(Double revenue, Integer artistCommissionRate) {
		return Math.floor(revenue * (artistCommissionRate / 100.0) * (1 - 0.033));
	}

	public LocalDate convertDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return LocalDate.parse(date + MONTH_PREFIX, formatter);
	}
}
