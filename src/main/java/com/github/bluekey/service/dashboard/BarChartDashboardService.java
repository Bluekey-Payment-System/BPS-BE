package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.common.MonthlyTrendDto;
import com.github.bluekey.dto.response.common.MonthlyTrendResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;

import com.github.bluekey.service.album.AlbumService;
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
public class BarChartDashboardService {

    private static final String MONTH_PREFIX = "01";
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final AlbumService albumService;

    @Transactional(readOnly = true)
    public MonthlyTrendResponseDto getAlbumBarChartDashboard(String startDate, String endDate
            , Long albumId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        albumRepository.findById(albumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        List<MonthlyTrendDto> contents = new ArrayList<>();

        if (member.isAdmin()) {
            contents = getMonthlyAlbumNetIncomeInfo(startDate, endDate, albumId);
        } else if (member.getRole() == MemberRole.ARTIST) {
            if (!albumService.isAlbumParticipant(albumId, memberId))
                throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
            contents = getMonthlyAlbumSettlementInfo(startDate, endDate, albumId, memberId);
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
                .orElseThrow(MemberNotFoundException::new);

        List<MonthlyTrendDto> contents = new ArrayList<>();

        if (member.isAdmin()) {
            contents = getMonthlyNetIncomeInfo(startDate, endDate);
        } else if (member.getRole() == MemberRole.ARTIST) {
            contents = getMonthlySettlementInfo(startDate, endDate, memberId);
        }
        return MonthlyTrendResponseDto
                .builder()
                .contents(contents)
                .build();
    }

    private List<MonthlyTrendDto> getMonthlyAlbumNetIncomeInfo(String startDate, String endDate, Long albumId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> amountMappedByDuration = getAdminAmountMappedByDuration(transactions, albumId);
        Map<String, Double> netIncomeAmountMappedByDuration = getAdminNetIncomeAmountMappedByDuration(transactions, albumId);

        return getNetIncomeContent(amountMappedByDuration, netIncomeAmountMappedByDuration, startDate, endDate);
    }

    private List<MonthlyTrendDto> getMonthlyAlbumSettlementInfo(String startDate, String endDate, Long albumId, Long memberId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> amountMappedByDuration = getArtistAmountMappedByDuration(transactions, albumId, memberId);
        Map<String, Double> settlementAmountMappedByDuration = getArtistSettlementAmountMappedByDuration(transactions, albumId, memberId);

        return getSettlementContent(amountMappedByDuration, settlementAmountMappedByDuration, startDate, endDate);
    }

    private List<MonthlyTrendDto> getMonthlyNetIncomeInfo(String startDate, String endDate) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> amountMappedByDuration = getAdminAmountMappedByDuration(transactions);
        Map<String, Double> netIncomeAmountMappedByDuration = getAdminNetIncomeAmountMappedByDuration(transactions);

        return getNetIncomeContent(amountMappedByDuration, netIncomeAmountMappedByDuration, startDate, endDate);
    }

    private List<MonthlyTrendDto> getMonthlySettlementInfo(String startDate, String endDate, Long memberId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);

        Map<String, Double> amountMappedByDuration = getArtistAmountMappedByDuration(transactions, memberId);
        Map<String, Double> settlementAmountMappedByDuration = getArtistSettlementAmountMappedByDuration(transactions, memberId);

        return getSettlementContent(amountMappedByDuration, settlementAmountMappedByDuration, startDate, endDate);
    }

    private List<MonthlyTrendDto> getSettlementContent(Map<String, Double> amountMappedByDuration,
                                                       Map<String, Double> settlementAmountMappedByDuration,
                                                       String startDate,
                                                       String endDate) {
        List<MonthlyTrendDto> contents = new ArrayList<>();
        List<Integer> months = new ArrayList<>();

        for (Map.Entry<String, Double> entry : amountMappedByDuration.entrySet()) {
            months.add(convertDate(entry.getKey()).getMonthValue());

        }

        for (Integer month : extractMonths(startDate, endDate)) {
            if (!months.contains(month)) {
                MonthlyTrendDto dto = MonthlyTrendDto
                        .builder()
                        .month(month)
                        .netIncome(0.0)
                        .revenue(0.0)
                        .settlement(0.0)
                        .build();
                contents.add(dto);
            } else {
                for (Map.Entry<String, Double> entry : amountMappedByDuration.entrySet()) {
                    if (convertDate(entry.getKey()).getMonthValue() == month) {
                        MonthlyTrendDto dto = MonthlyTrendDto
                                .builder()
                                .month(convertDate(entry.getKey()).getMonthValue())
                                .settlement(settlementAmountMappedByDuration.get(entry.getKey()))
                                .revenue(entry.getValue())
                                .netIncome(0.0)
                                .build();
                        contents.add(dto);

                    }

                }
            }
        }


        return contents;
    }

    private List<MonthlyTrendDto> getNetIncomeContent(Map<String, Double> amountMappedByDuration,
                                                      Map<String, Double> netIncomeAmountMappedByDuration, String startDate, String endDate) {
        List<MonthlyTrendDto> contents = new ArrayList<>();
        List<Integer> months = new ArrayList<>();

        for (Map.Entry<String, Double> entry : amountMappedByDuration.entrySet()) {
            months.add(convertDate(entry.getKey()).getMonthValue());
        }

        for (Integer month : extractMonths(startDate, endDate)) {
            if (!months.contains(month)) {
                MonthlyTrendDto dto = MonthlyTrendDto
                        .builder()
                        .month(month)
                        .netIncome(0.0)
                        .revenue(0.0)
                        .settlement(0.0)
                        .build();
                contents.add(dto);
            } else {
                for (Map.Entry<String, Double> entry : amountMappedByDuration.entrySet()) {
                    if (convertDate(entry.getKey()).getMonthValue() == month) {
                        MonthlyTrendDto dto = MonthlyTrendDto
                                .builder()
                                .month(convertDate(entry.getKey()).getMonthValue())
                                .netIncome(netIncomeAmountMappedByDuration.get(entry.getKey()))
                                .revenue(entry.getValue())
                                .settlement(0.0)
                                .build();
                        contents.add(dto);

                    }

                }
            }
        }

        return contents;
    }

    private List<Integer> extractMonths(String startDate, String endDate) {
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

    public LocalDate convertDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
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

    private Map<String, Double> getAdminAmountMappedByDuration(List<Transaction> transactions) {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble(Transaction::getAmount)
                        ));
    }

    private Map<String, Double> getAdminNetIncomeAmountMappedByDuration(List<Transaction> transactions) {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble((t) -> getNetIncome(t.getAmount(), t.getTrackMember().getCommissionRate()))
                        ));
    }

    private Map<String, Double> getArtistSettlementAmountMappedByDuration(List<Transaction> transactions, Long memberId) {
        return transactions.stream()
                .filter(transaction -> transaction.getTrackMember().isArtistTrack())
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble((t) -> getSettlement(t.getAmount(), t.getTrackMember().getCommissionRate()))
                        ));
    }

    private Map<String, Double> getArtistAmountMappedByDuration(List<Transaction> transactions, Long memberId) {
        return transactions.stream()
                .filter(transaction -> transaction.getTrackMember().isArtistTrack())
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble(Transaction::getAmount))
                );
    }
}
