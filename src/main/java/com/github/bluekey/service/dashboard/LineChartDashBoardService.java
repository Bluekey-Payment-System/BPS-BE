package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.album.AlbumTrackAccountsDto;
import com.github.bluekey.dto.album.AlbumTrackMonthlyTrendInfoDto;
import com.github.bluekey.dto.response.album.AlbumTrackAccountsResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.track.Track;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineChartDashBoardService {

	private static final String MONTH_PREFIX = "01";
	private final TransactionRepository transactionRepository;
	private final MemberRepository memberRepository;
	private final AlbumRepository albumRepository;

	@Transactional(readOnly = true)
	public AlbumTrackAccountsResponseDto getAlbumLineChartDashboard(String startDate, String endDate
			, Long albumId, Long memberId) {

		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);

		albumRepository.findById(albumId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

		List<AlbumTrackAccountsDto> tracks = new ArrayList<>();

		if (member.isAdmin()) {
			tracks = getAdminAlbumTrackAccounts(startDate, endDate, albumId);
		} else if (member.getRole() == MemberRole.ARTIST) {
			tracks = getArtistAlbumTrackAccounts(startDate, endDate, albumId, memberId);
		}
		return AlbumTrackAccountsResponseDto.builder()
				.tracks(tracks)
				.build();
	}

	private List<AlbumTrackAccountsDto> getAdminAlbumTrackAccounts(String startDate, String endDate, Long albumId) {

		List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

		List<Track> sortedTracks = getSortedTrack(transactions, albumId, null);

		return sortedTracks.stream()
				.map(track -> AlbumTrackAccountsDto
						.builder()
						.trackId(track.getId())
						.name(track.getName())
						.enName(track.getEnName())
						.monthlyTrend(getAdminMonthlyTrend(transactions, track.getId()))
						.build())
				.collect(Collectors.toList());
	}

	private List<AlbumTrackMonthlyTrendInfoDto> getAdminMonthlyTrend(List<Transaction> transactions, Long trackId) {
		return transactions
				.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getId().equals(trackId))
				.map(transaction -> AlbumTrackMonthlyTrendInfoDto
						.builder()
						.month(convertDate(transaction.getDuration()).getMonthValue())
						.revenue(transaction.getAmount())
						.build())
				.collect(Collectors.toList());
	}

	private List<AlbumTrackAccountsDto> getArtistAlbumTrackAccounts(String startDate, String endDate, Long albumId, Long memberId) {

		List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

		List<Track> sortedTracks = getSortedTrack(transactions, albumId, memberId);

		return sortedTracks.stream()
				.map(track -> AlbumTrackAccountsDto
						.builder()
						.trackId(track.getId())
						.name(track.getName())
						.enName(track.getEnName())
						.monthlyTrend(getArtistMonthlyTrend(transactions, track.getId()))
						.build())
				.collect(Collectors.toList());
	}

	private List<AlbumTrackMonthlyTrendInfoDto> getArtistMonthlyTrend(List<Transaction> transactions, Long trackId) {
		return transactions
				.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getId().equals(trackId))
				.map(transaction -> AlbumTrackMonthlyTrendInfoDto
						.builder()
						.month(convertDate(transaction.getDuration()).getMonthValue())
						.settlement(getSettlement(transaction.getAmount(), transaction.getTrackMember().getCommissionRate()))
						.build())
				.collect(Collectors.toList());
	}

	public LocalDate convertDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return LocalDate.parse(date + MONTH_PREFIX, formatter);
	}

	private Double getSettlement(Double revenue, Integer commissionRate) {
		return revenue * (commissionRate / 100.0);
	}

	private List<Track> getSortedTrack(List<Transaction> transactions, Long albumId, Long memberId) {
		List<Track> tracks;
		if (memberId == null) {
			tracks = getAdminViewTracks(transactions, albumId);
		} else {
			tracks = getArtistViewTracks(transactions, albumId, memberId);
		}
		return tracks.stream()
				.sorted(Comparator.comparing(Track::getId))
				.collect(Collectors.toList());
	}

	private List<Track> getAdminViewTracks(List<Transaction> transactions, Long albumId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.map(transaction -> transaction.getTrackMember().getTrack())
				.distinct()
				.collect(Collectors.toList());
	}

	private List<Track> getArtistViewTracks(List<Transaction> transactions, Long albumId, Long memberId) {
		return transactions.stream()
				.filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
				.filter(transaction -> transaction.getTrackMember().isArtistTrack())
				.filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
				.map(transaction -> transaction.getTrackMember().getTrack())
				.distinct()
				.collect(Collectors.toList());
	}
}