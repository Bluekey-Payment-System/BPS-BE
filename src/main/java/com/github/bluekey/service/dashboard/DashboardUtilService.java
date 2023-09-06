package com.github.bluekey.service.dashboard;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardUtilService {
	private static final String MONTH_PREFIX = "01";
	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final String DATE_FORMAT_WITHOUT_DAY = "yyyyMM";
	private static final double TAX = 0.033;

	/* ------------- Calculate ------------- */
	public Integer getCompanyNetIncome(Double revenue, Integer artistCommissionRate) {
		int companyCommissionRate = 100 - artistCommissionRate;
		return (int) Math.floor(revenue * (companyCommissionRate / 100.0));
	}

	public Integer getArtistSettlement(Double revenue, Integer artistCommissionRate) {
		return (int) Math.floor(revenue * (artistCommissionRate / 100.0) * (1 - TAX));
	}

	public Double getGrowthRate(Double previousMonthAmount, double amount) {
		if (previousMonthAmount == null || amount == 0.0) {
			return null;
		}

		if (previousMonthAmount == 0.0) {
			return null;
		}

		double percentage = (amount - previousMonthAmount) / previousMonthAmount * 100;
		if (0 < percentage && percentage < 10) {
			return Math.floor(percentage * 10) / 10;
		}
		return Math.floor(percentage);
	}

	public double getProportion(double amount, double totalAmount) {
		double percentage = (amount / totalAmount) * 100;
		if (0 < percentage && percentage < 1) {
			return Math.floor(percentage * 10) / 10;
		}
		return Math.floor(percentage);
	}

	public Integer getRevenue(double revenue) {
		return (int) Math.floor(revenue);
	}

	/* ------------- Date ------------- */
	public LocalDate convertDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return LocalDate.parse(date + MONTH_PREFIX, formatter);
	}

	public String getPreviousMonth(String monthly) {
		LocalDate date = LocalDate.parse(monthly + MONTH_PREFIX, DateTimeFormatter.ofPattern(DATE_FORMAT));
		LocalDate previousMonth = date.minusMonths(1);
		return previousMonth.format(DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_DAY));
	}

	public List<Integer> extractMonths(String startDate, String endDate) {
		List<Integer> months = new ArrayList<>();

		int startYear = Integer.parseInt(startDate.substring(0, 4));
		int startMonth = Integer.parseInt(startDate.substring(4, 6));
		int endYear = Integer.parseInt(endDate.substring(0, 4));
		int endMonth = Integer.parseInt(endDate.substring(4, 6));

		for (int year = startYear; year <= endYear; year++) {
			int currentStartMonth = (year == startYear) ? startMonth : 1;
			int currentEndMonth = (year == endYear) ? endMonth : 12;

			for (int month = currentStartMonth; month <= currentEndMonth; month++) {
				months.add(month);
			}
		}

		return months;
	}
	/* ------------- Pagination ------------- */

	public static <T> List<T> getPage(List<T> sources, int page, int pageSize) {
		if (pageSize <= 0) {
			throw new IllegalArgumentException("invalid page size: " + pageSize);
		}

		int fromIndex = (page) * pageSize;
		if (sources == null || sources.size() <= fromIndex) {
			return Collections.emptyList();
		}

		// toIndex exclusive
		return sources.subList(fromIndex, Math.min(fromIndex + pageSize, sources.size()));
	}

	/* ------------- Grouping ------------- */
	public Map<Track, Double> getAmountGroupedByTrackFilteredByMemberId(List<Transaction> transactions, Long memberId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.collect(Collectors.groupingBy(
						transaction -> transaction.getTrackMember().getTrack(),
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<Track, Double> getAmountGroupedByTrackFilteredByMemberIdNotNull(List<Transaction> transactions) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.collect(Collectors.groupingBy(
						transaction -> transaction.getTrackMember().getTrack(),
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<Track, Double> getAmountGroupedByTrackFilteredByMemberIdAndAlbumId(List<Transaction> transactions, Long memberId, Long albumId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.collect(Collectors.groupingBy(
						transaction -> transaction.getTrackMember().getTrack(),
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<Track, Double> getAmountGroupedByTrackFilteredByAlbumId(List<Transaction> transactions, Long albumId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.collect(Collectors.groupingBy(
						transaction -> transaction.getTrackMember().getTrack(),
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<TrackMember, Double> getAmountGroupedByTrackMember(List<Transaction> transactions) {
		return transactions.stream()
				.collect(Collectors.groupingBy(
						Transaction::getTrackMember,
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<TrackMember, Double> getAmountGroupedByTrackMemberFilteredByMemberIdNotNull(List<Transaction> transactions) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.collect(Collectors.groupingBy(
						Transaction::getTrackMember,
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<TrackMember, Double> getAmountGroupedByTrackMemberFilteredByMemberId(List<Transaction> transactions, Long memberId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.collect(Collectors.groupingBy(
						Transaction::getTrackMember,
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<TrackMember, Double> getAmountGroupedByTrackMemberFilteredByMemberIdAndAlbumId(
			List<Transaction> transactions, Long memberId, Long albumId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.collect(Collectors.groupingBy(
						Transaction::getTrackMember,
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<TrackMember, Double> getAmountGroupedByTrackMemberFilteredByAlbumId(List<Transaction> transactions, Long albumId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.collect(Collectors.groupingBy(
						Transaction::getTrackMember,
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<Album, Double> getAmountGroupedByAlbumMemberFilteredByMemberId(List<Transaction> transactions, Long memberId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getMemberId() != null)
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.collect(Collectors.groupingBy(
						transaction -> transaction.getTrackMember().getTrack().getAlbum(),
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<Long, Double> getAmountGroupedByMemberId(List<Transaction> transactions) {
		return transactions.stream()
				.filter((transaction -> transaction.getTrackMember().getMemberId() != null))
				.collect(Collectors.groupingBy(
						transaction -> transaction.getTrackMember().getMemberId(),
						Collectors.summingDouble(Transaction::getAmount)
				));
	}

	public Map<Track, Double> getSortedAmountGroupedByTrack(Map<Track, Double> amountGroupedByTrack) {
		return amountGroupedByTrack.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
	}

	public Map<TrackMember, Double> getSortedAmountGroupedByTrackMember(Map<TrackMember, Double> amountGroupedByTrackMember) {
		return amountGroupedByTrackMember.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
	}

	public Map<Album, Double> getSortedAmountGroupedByAlbum(Map<Album, Double> amountGroupedByAlbum) {
		return amountGroupedByAlbum.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
	}

	public Map<Long, Double> getSortedAmountGroupedByMemberId(Map<Long, Double> amountGroupedByMemberId) {
		return amountGroupedByMemberId.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
	}
}
