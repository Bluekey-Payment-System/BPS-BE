package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.album.AlbumTrackTrendDto;
import com.github.bluekey.dto.album.AlbumTrackMonthlyTrendInfoDto;
import com.github.bluekey.dto.response.album.AlbumTrackTrendResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;

import com.github.bluekey.service.album.AlbumService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineChartDashBoardService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final AlbumService albumService;
    private final DashboardUtilService dashboardUtilService;

    @Transactional(readOnly = true)
    public AlbumTrackTrendResponseDto getAlbumLineChartDashboard(String startDate, String endDate
            , Long albumId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        albumRepository.findById(albumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        List<AlbumTrackTrendDto> tracks = new ArrayList<>();

        if (member.isAdmin()) {
            tracks = getAdminAlbumTrackTrend(startDate, endDate, albumId);
        } else if (member.getRole() == MemberRole.ARTIST) {
            if (!albumService.isAlbumParticipant(albumId, memberId))
                throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
            tracks = getArtistAlbumTrackTrend(startDate, endDate, albumId, memberId);
        }
        return AlbumTrackTrendResponseDto.builder()
                .tracks(tracks)
                .build();
    }

    private List<AlbumTrackTrendDto> getAdminAlbumTrackTrend(String startDate, String endDate, Long albumId) {

        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        List<Track> sortedTracks = getSortedTrack(transactions, albumId, null);

        return sortedTracks.stream()
                .map(track -> AlbumTrackTrendDto
                        .builder()
                        .trackId(track.getId())
                        .name(track.getName())
                        .enName(track.getEnName())
                        .monthlyTrend(getAdminMonthlyTrend(transactions, track.getId(), startDate, endDate))
                        .build())
                .collect(Collectors.toList());
    }

    private List<AlbumTrackMonthlyTrendInfoDto> getAdminMonthlyTrend(List<Transaction> transactions, Long trackId, String startDate, String endDate) {
        List<AlbumTrackMonthlyTrendInfoDto> albumTrackMonthlyTrends = new ArrayList<>();
        List<AlbumTrackMonthlyTrendInfoDto> trends = transactions
                .stream()
                .filter(transaction -> transaction.getTrack().getId().equals(trackId))
                .map(transaction -> AlbumTrackMonthlyTrendInfoDto
                        .builder()
                        .month(dashboardUtilService.convertDate(transaction.getDuration()).getMonthValue())
                        .revenue(dashboardUtilService.getRevenue(transaction.getAmount()))
                        .settlement(0)
                        .build())
                .collect(Collectors.toList());
        for (Integer month : dashboardUtilService.extractMonths(startDate, endDate)) {

            Optional<AlbumTrackMonthlyTrendInfoDto> matchedMonthTrend = trends.stream()
                    .filter(trend -> trend.getMonth().equals(month))
                    .findFirst();
            if (matchedMonthTrend.isPresent()) {
                albumTrackMonthlyTrends.add(matchedMonthTrend.get());
            } else {
                AlbumTrackMonthlyTrendInfoDto trend = AlbumTrackMonthlyTrendInfoDto
                        .builder()
                        .month(month)
                        .revenue(0)
                        .settlement(0)
                        .build();
                albumTrackMonthlyTrends.add(trend);
            }
        }

        return albumTrackMonthlyTrends;
    }

    private List<AlbumTrackTrendDto> getArtistAlbumTrackTrend(String startDate, String endDate, Long albumId, Long memberId) {

        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        List<Track> sortedTracks = getSortedTrack(transactions, albumId, memberId);

        return sortedTracks.stream()
                .map(track -> AlbumTrackTrendDto
                        .builder()
                        .trackId(track.getId())
                        .name(track.getName())
                        .enName(track.getEnName())
                        .monthlyTrend(getArtistMonthlyTrend(transactions, track.getId(), startDate, endDate))
                        .build())
                .collect(Collectors.toList());
    }

    private List<AlbumTrackMonthlyTrendInfoDto> getArtistMonthlyTrend(List<Transaction> transactions, Long trackId, String startDate, String endDate) {
        List<AlbumTrackMonthlyTrendInfoDto> albumTrackMonthlyTrends = new ArrayList<>();

        List<AlbumTrackMonthlyTrendInfoDto> trends = transactions
                .stream()
                .filter(transaction -> transaction.getTrack().getId().equals(trackId))
                .map(transaction -> AlbumTrackMonthlyTrendInfoDto
                        .builder()
                        .month(dashboardUtilService.convertDate(transaction.getDuration()).getMonthValue())
                        .revenue(0)
                        .settlement(dashboardUtilService.getArtistSettlement(transaction.getAmount(), transaction.getTrackMember().getCommissionRate()))
                        .build())
                .collect(Collectors.toList());
        for (Integer month : dashboardUtilService.extractMonths(startDate, endDate)) {

            Optional<AlbumTrackMonthlyTrendInfoDto> matchedMonthTrend = trends.stream()
                    .filter(trend -> trend.getMonth().equals(month))
                    .findFirst();
            if (matchedMonthTrend.isPresent()) {
                albumTrackMonthlyTrends.add(matchedMonthTrend.get());
            } else {
                AlbumTrackMonthlyTrendInfoDto trend = AlbumTrackMonthlyTrendInfoDto
                        .builder()
                        .month(month)
                        .revenue(0)
                        .settlement(0)
                        .build();
                albumTrackMonthlyTrends.add(trend);
            }
        }

        return albumTrackMonthlyTrends;
    }


    private List<Track> getSortedTrack(List<Transaction> transactions, Long albumId, Long memberId) {
        Stream<Transaction> transactionStream = getFilteredTransactionStream(transactions, albumId);
        if (memberId != null) {
            transactionStream = getFilteredMemberTransactionStream(transactionStream, memberId);
        }

        return transactionStream.map(Transaction::getTrack)
                .distinct()
                .sorted(Comparator.comparing(Track::getId))
                .collect(Collectors.toList());
    }

    private Stream<Transaction> getFilteredTransactionStream(List<Transaction> transactions, Long albumId) {
        return transactions.stream()
                .filter(transaction -> dashboardUtilService.hasAlbumIdInTransaction(transaction, albumId));
    }

    private Stream<Transaction> getFilteredMemberTransactionStream(Stream<Transaction> transactionStream, Long memberId) {
        return transactionStream
                .filter(transaction -> dashboardUtilService.hasMemberIdInTrackMembers(transaction, memberId));
    }
}
