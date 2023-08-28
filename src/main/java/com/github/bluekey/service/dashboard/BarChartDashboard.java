package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.album.AlbumMonthlyAccountsDto;
import com.github.bluekey.dto.response.album.AlbumMonthlyAccontsReponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarChartDashboard {

	private static final String MONTH_PREFIX = "-01";
	private final TransactionRepository transactionRepository;
	private final MemberRepository memberRepository;
	private final AlbumRepository albumRepository;

	@Transactional(readOnly = true)
	public AlbumMonthlyAccontsReponseDto getAlbumBarChartDashboard(String startDate, String endDate
			, Long albumId, Long memberId) {

		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);

		albumRepository.findById(albumId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

		List<AlbumMonthlyAccountsDto> contents = new ArrayList<>();

		if (member.getRole() == MemberRole.ADMIN || member.getRole() == MemberRole.SUPER_ADMIN) {
			contents = getAdminAlbumMonthlyAccounts(startDate, endDate, albumId);
		} else if (member.getRole() == MemberRole.ARTIST) {
			contents = getArtistAlbumMonthlyAccounts(startDate, endDate, albumId, memberId);
		}
		return AlbumMonthlyAccontsReponseDto
				.builder()
				.contents(contents)
				.build();
	}

	private List<AlbumMonthlyAccountsDto> getAdminAlbumMonthlyAccounts(String startDate, String endDate, Long albumId) {
		List<AlbumMonthlyAccountsDto> contents = new ArrayList<>();

		List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

		Map<String, Double> amountMappedByDuration = getAdminAmountMappedByDuration(transactions, albumId);
		Map<String, Double> netIncomeAmountMappedByDuration = getAdminNetIncomeAmountMappedByDuration(transactions, albumId);

		for (Map.Entry<String, Double> entry : amountMappedByDuration.entrySet()) {
			AlbumMonthlyAccountsDto dto = AlbumMonthlyAccountsDto
					.builder()
					.month(convertDate(entry.getKey()).getMonthValue())
					.netIncome(netIncomeAmountMappedByDuration.get(entry.getKey()))
					.revenue(entry.getValue())
					.build();
			contents.add(dto);
		}
		return contents;
	}

	private List<AlbumMonthlyAccountsDto> getArtistAlbumMonthlyAccounts(String startDate, String endDate, Long albumId, Long memberId) {
		List<AlbumMonthlyAccountsDto> contents = new ArrayList<>();
		List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

		Map<String, Double> amountMappedByDuration = getArtistAmountMappedByDuration(transactions, albumId, memberId);
		Map<String, Double> settlementAmountMappedByDuration = getArtistSettlementAmountMappedByDuration(transactions, albumId, memberId);

		for (Map.Entry<String, Double> entry : amountMappedByDuration.entrySet()) {
			AlbumMonthlyAccountsDto dto = AlbumMonthlyAccountsDto
					.builder()
					.month(convertDate(entry.getKey()).getMonthValue())
					.settlement(settlementAmountMappedByDuration.get(entry.getKey()))
					.revenue(entry.getValue())
					.build();
			contents.add(dto);
		}
		return contents;
	}

	public LocalDate convertDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(date + MONTH_PREFIX, formatter);
	}

	private Double getNetIncome(Double revenue, Integer commissionRate) {
		return revenue * ((100 - commissionRate) / 100.0);
	}

	private Double getSettlement(Double revenue, Integer commissionRate) {
		return revenue * (commissionRate / 100.0);
	}

	private Map<String, Double> getAdminAmountMappedByDuration(List<Transaction> transactions, Long albumId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.collect(
						Collectors.groupingBy(
								Transaction::getDuration,
								Collectors.summingDouble(Transaction::getAmount)
				));
	}

	private Map<String, Double> getAdminNetIncomeAmountMappedByDuration(List<Transaction> transactions, Long albumId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.collect(
						Collectors.groupingBy(
								Transaction::getDuration,
								Collectors.summingDouble((t) -> getNetIncome(t.getAmount(), t.getTrackMember().getCommissionRate()))
						));
	}

	private Map<String, Double> getArtistSettlementAmountMappedByDuration(List<Transaction> transactions, Long albumId, Long memberId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.filter(transaction -> transaction.getTrackMember().isArtistTrack())
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.collect(
						Collectors.groupingBy(
								Transaction::getDuration,
								Collectors.summingDouble((t) -> getSettlement(t.getAmount(), t.getTrackMember().getCommissionRate()))
						));
	}

	private Map<String, Double> getArtistAmountMappedByDuration(List<Transaction> transactions, Long albumId, Long memberId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.filter(transaction -> transaction.getTrackMember().isArtistTrack())
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.collect(
						Collectors.groupingBy(
								Transaction::getDuration,
								Collectors.summingDouble(Transaction::getAmount))
						);
	}
}
