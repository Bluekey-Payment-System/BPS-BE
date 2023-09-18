package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.common.MonthlyTrendDto;
import com.github.bluekey.dto.response.common.MonthlyTrendResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;

import com.github.bluekey.service.album.AlbumService;
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
public class BarChartDashboardService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final AlbumService albumService;
    private final DashboardUtilService dashboardUtilService;

    @Transactional(readOnly = true)
    public MonthlyTrendResponseDto getAlbumBarChartDashboard(String startDate, String endDate
            , Long albumId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        albumRepository.findById(albumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        List<MonthlyTrendDto> contents = new ArrayList<>();

        if (member.isAdmin()) {
            contents = getMonthlyAlbumNetIncome(startDate, endDate, albumId);
        } else if (member.getRole() == MemberRole.ARTIST) {
            if (!albumService.isAlbumParticipant(albumId, memberId))
                throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
            contents = getMonthlyAlbumSettlement(startDate, endDate, albumId, memberId);
        }
        return MonthlyTrendResponseDto
                .builder()
                .contents(contents)
                .build();
    }

    @Transactional(readOnly = true)
    public MonthlyTrendResponseDto getBarChartDashboard(String startDate, String endDate
            , Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<MonthlyTrendDto> contents = new ArrayList<>();

        if (member.isAdmin()) {
            contents = getMonthlyNetIncome(startDate, endDate);
        } else if (member.getRole() == MemberRole.ARTIST) {
            contents = getMonthlySettlement(startDate, endDate, memberId);
        }
        return MonthlyTrendResponseDto
                .builder()
                .contents(contents)
                .build();
    }

    private List<MonthlyTrendDto> getMonthlyAlbumNetIncome(String startDate, String endDate, Long albumId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> adminAmountGroupedByMonthForAlbum = getAdminAmountGroupedByMonthForAlbum(transactions, albumId);
        Map<String, Integer> netIncomeGroupedByMonthForAlbum = getAdminNetIncomeGroupedByMonthForAlbum(transactions, albumId);

        return getNetIncomeContent(adminAmountGroupedByMonthForAlbum, netIncomeGroupedByMonthForAlbum, startDate, endDate);
    }

    private List<MonthlyTrendDto> getMonthlyAlbumSettlement(String startDate, String endDate, Long albumId, Long memberId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> artistAmountGroupedByMonthForAlbum = getArtistAmountGroupedByMonthForAlbum(transactions, albumId, memberId);
        Map<String, Integer> artistSettlementGroupedByMonthForAlbum = getArtistSettlementGroupedByMonthForAlbum(transactions, albumId, memberId);

        return getSettlementContent(artistAmountGroupedByMonthForAlbum, artistSettlementGroupedByMonthForAlbum, startDate, endDate);
    }

    private List<MonthlyTrendDto> getMonthlyNetIncome(String startDate, String endDate) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> adminAmountGroupedByMonth = getAdminAmountGroupedByMonth(transactions);
        Map<String, Integer> netIncomeGroupedByMonth = getAdminNetIncomeGroupedByMonth(transactions);

        return getNetIncomeContent(adminAmountGroupedByMonth, netIncomeGroupedByMonth, startDate, endDate);
    }

    private List<MonthlyTrendDto> getMonthlySettlement(String startDate, String endDate, Long memberId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> artistAmountGroupedByMonth = getArtistAmountGroupedByMonth(transactions, memberId);
        Map<String, Integer> artistSettlementGroupedByMonth = getArtistSettlementGroupedByMonth(transactions, memberId);

        return getSettlementContent(artistAmountGroupedByMonth, artistSettlementGroupedByMonth, startDate, endDate);
    }

    private List<MonthlyTrendDto> getSettlementContent(Map<String, Double> artistAmountGroupedByMonth,
                                                       Map<String, Integer> artistSettlementGroupedByMonth,
                                                       String startDate,
                                                       String endDate) {
        List<MonthlyTrendDto> contents = new ArrayList<>();

        List<Integer> months = artistAmountGroupedByMonth.keySet().stream()
                .map(date -> dashboardUtilService.convertDate(date).getMonthValue())
                .collect(Collectors.toList());

        for (Integer month : dashboardUtilService.extractMonths(startDate, endDate)) {
            if (!months.contains(month)) {
                MonthlyTrendDto dto = MonthlyTrendDto
                        .builder()
                        .month(month)
                        .netIncome(0)
                        .revenue(0)
                        .settlement(0)
                        .build();
                contents.add(dto);
            } else {
                for (Map.Entry<String, Double> entry : artistAmountGroupedByMonth.entrySet()) {
                    if (dashboardUtilService.convertDate(entry.getKey()).getMonthValue() == month) {
                        MonthlyTrendDto dto = MonthlyTrendDto
                                .builder()
                                .month(dashboardUtilService.convertDate(entry.getKey()).getMonthValue())
                                .settlement(artistSettlementGroupedByMonth.get(entry.getKey()))
                                .revenue(dashboardUtilService.getRevenue(entry.getValue()))
                                .netIncome(0)
                                .build();
                        contents.add(dto);
                    }
                }
            }
        }

        return contents;
    }

    private List<MonthlyTrendDto> getNetIncomeContent(Map<String, Double> adminAmountGroupedByMonth,
                                                      Map<String, Integer> netIncomeGroupedByMonth, String startDate, String endDate) {
        List<MonthlyTrendDto> contents = new ArrayList<>();

        List<Integer> months = adminAmountGroupedByMonth.keySet().stream()
                .map(date -> dashboardUtilService.convertDate(date).getMonthValue())
                .collect(Collectors.toList());

        for (Integer month : dashboardUtilService.extractMonths(startDate, endDate)) {
            if (!months.contains(month)) {
                MonthlyTrendDto dto = MonthlyTrendDto
                        .builder()
                        .month(month)
                        .netIncome(0)
                        .revenue(0)
                        .settlement(0)
                        .build();
                contents.add(dto);
            } else {
                for (Map.Entry<String, Double> entry : adminAmountGroupedByMonth.entrySet()) {
                    if (dashboardUtilService.convertDate(entry.getKey()).getMonthValue() == month) {
                        MonthlyTrendDto dto = MonthlyTrendDto
                                .builder()
                                .month(dashboardUtilService.convertDate(entry.getKey()).getMonthValue())
                                .netIncome(netIncomeGroupedByMonth.get(entry.getKey()))
                                .revenue(dashboardUtilService.getRevenue(entry.getValue()))
                                .settlement(0)
                                .build();
                        contents.add(dto);
                    }

                }
            }
        }

        return contents;
    }

    private Map<String, Double> getAdminAmountGroupedByMonthForAlbum(List<Transaction> transactions, Long albumId) {
        return transactions.stream()
                .filter(transaction -> transaction.getTrack().getAlbum().getId().equals(albumId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble(Transaction::getAmount)
                        ));
    }

    private Map<String, Integer> getAdminNetIncomeGroupedByMonthForAlbum(List<Transaction> transactions, Long albumId) {
        return transactions.stream()
                .filter(transaction -> transaction.getTrack().getAlbum().getId().equals(albumId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingInt((t) -> dashboardUtilService.getCompanyNetIncome(t.getAmount(), t.getTrackMember().getCommissionRate()))
                        ));
    }

    private Map<String, Integer> getArtistSettlementGroupedByMonthForAlbum(List<Transaction> transactions, Long albumId, Long memberId) {
        return transactions.stream()
                .filter(transaction -> dashboardUtilService.hasAlbumIdInTransaction(transaction, albumId))
                .filter(transaction -> dashboardUtilService.hasMemberIdInTrackMembers(transaction, memberId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingInt((t) ->
                                        dashboardUtilService.getArtistSettlement(t.getAmount(), getCommissionRateByTrack(t.getTrack(), memberId)))
                        ));
    }

    private Map<String, Double> getArtistAmountGroupedByMonthForAlbum(List<Transaction> transactions, Long albumId, Long memberId) {
        return transactions.stream()
                .filter(transaction -> dashboardUtilService.hasAlbumIdInTransaction(transaction, albumId))
                .filter(transaction -> dashboardUtilService.hasMemberIdInTrackMembers(transaction, memberId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble(Transaction::getAmount))
                );
    }

    private Map<String, Double> getAdminAmountGroupedByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble(Transaction::getAmount)
                        ));
    }

    private Map<String, Integer> getAdminNetIncomeGroupedByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingInt((t) -> dashboardUtilService
                                        .getCompanyNetIncome(t.getAmount(), getTotalCommissionRateByTrack(t.getTrack())))
                        ));
    }

    private Map<String, Integer> getArtistSettlementGroupedByMonth(List<Transaction> transactions, Long memberId) {
        return transactions.stream()
                .filter(transaction -> dashboardUtilService.hasMemberIdInTrackMembers(transaction, memberId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingInt((t) -> dashboardUtilService
                                        .getArtistSettlement(t.getAmount(), getCommissionRateByTrack(t.getTrack(), memberId)))
                        ));
    }

    private Map<String, Double> getArtistAmountGroupedByMonth(List<Transaction> transactions, Long memberId) {
        return transactions.stream()
                .filter(transaction -> dashboardUtilService.hasMemberIdInTrackMembers(transaction, memberId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble(Transaction::getAmount))
                );
    }

    private Integer getCommissionRateByTrack(Track track, Long memberId) {
        return track.getTrackMembers()
                .stream()
                .filter(trackMember -> trackMember.getMemberId().equals(memberId))
                .findFirst()
                .get()
                .getCommissionRate();
    }

    private Integer getTotalCommissionRateByTrack(Track track) {
        return track.getTrackMembers()
                .stream()
                .mapToInt(TrackMember::getCommissionRate)
                .sum();
    }
}
