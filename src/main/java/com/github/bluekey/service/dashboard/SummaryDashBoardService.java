package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.artist.BestArtistDto;
import com.github.bluekey.dto.common.TotalAndGrowthDto;
import com.github.bluekey.dto.response.common.DashboardTotalInfoResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryDashBoardService {
    private static final String MONTH_PREFIX = "-01";
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;


    public DashboardTotalInfoResponseDto getAdminDashBoardSummaryInformation(String monthly, Long memberId) {
        BestArtistDto bestArtist = getBestArtist(monthly);
        double totalRevenue = 0.0;
        double totalPreviousMonthRevenue = 0.0;
        double revenueGrowthRate;

        double newIncome;
        double previousMonthNewIncome;
        double newIncomeGrowthRate;

        double settlementAmount;
        double previousMonthSettlementAmount;
        double settlementAmountGrowthRate;

        Map<TrackMember, Double> trackMemberMappedByAmount = getTrackMemberMappedByAmount(monthly);
        Map<TrackMember, Double> previousMonthlyTrackMemberMappedByAmount = getPreviousMonthlyTrackMemberMappedByAmount(monthly);

        for (Map.Entry<TrackMember, Double> entry : trackMemberMappedByAmount.entrySet()) {
            totalRevenue += entry.getValue();
        }

        for (Map.Entry<TrackMember, Double> entry : previousMonthlyTrackMemberMappedByAmount.entrySet()) {
            totalPreviousMonthRevenue += entry.getValue();
        }
        revenueGrowthRate = getGrowthRate(totalPreviousMonthRevenue, totalRevenue);

        newIncome = getIncome(totalRevenue);
        previousMonthNewIncome = getIncome(totalPreviousMonthRevenue);
        newIncomeGrowthRate = getGrowthRate(previousMonthNewIncome, newIncome);

        settlementAmount = getSettlementAmount(trackMemberMappedByAmount, memberId);
        previousMonthSettlementAmount = getSettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);
        settlementAmountGrowthRate = getGrowthRate(previousMonthSettlementAmount, settlementAmount);

        log.info("data = {} {} {} {} {} {}", totalRevenue, totalPreviousMonthRevenue, newIncome, previousMonthNewIncome, settlementAmount, previousMonthSettlementAmount);
        return DashboardTotalInfoResponseDto.builder()
                .bestArtist(bestArtist)
                .revenue(
                        TotalAndGrowthDto.builder()
                                .totalAmount((long) totalRevenue)
                                .growthRate(revenueGrowthRate)
                                .build())
                .newIncome(
                        TotalAndGrowthDto.builder()
                                .totalAmount((long) newIncome)
                                .growthRate(newIncomeGrowthRate)
                                .build()
                )
                .settlementAmount(
                        TotalAndGrowthDto.builder()
                                .totalAmount((long) settlementAmount)
                                .growthRate(settlementAmountGrowthRate)
                                .build()
                )
                .build();
    }

    private Double getIncome(Double revenue) {
        return revenue - (revenue * 33 / 1000);
    }

    private double getSettlementAmount(Map<TrackMember, Double> trackMemberMappedByAmount, Long memberId) {
        double totalSettlementAmount = 0.0;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException();
        });


        for (Map.Entry<TrackMember, Double> entry : trackMemberMappedByAmount.entrySet()) {
            TrackMember trackMember = entry.getKey();
            Double amount = entry.getValue();
            if (member.getRole().equals(MemberRole.ARTIST)) {
                totalSettlementAmount += (amount * trackMember.getCommissionRate() / 100);
            } else {
                totalSettlementAmount += amount - (amount * trackMember.getCommissionRate() / 100);
            }
        }
        return totalSettlementAmount;
    }

    private String getPreviousMonth(String monthly) {
        LocalDate date = LocalDate.parse(monthly + MONTH_PREFIX, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate previousMonth = date.minusMonths(1);
        return previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    private Map<TrackMember, Double> getTrackMemberMappedByAmount(String monthly) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        Map<TrackMember, Double> trackMemberMappedByAmount = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getTrackMember,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        return trackMemberMappedByAmount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<TrackMember, Double> getPreviousMonthlyTrackMemberMappedByAmount(String monthly) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(getPreviousMonth(monthly));
        Map<TrackMember, Double> trackMemberMappedByAmount = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getTrackMember,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        return trackMemberMappedByAmount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private BestArtistDto getBestArtist(String monthly) {
        String previousMonthly = getPreviousMonth(monthly);

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        Map<Long, Double> artistMappedByAmount = transactions.stream()
                .filter((transaction -> transaction.getTrackMember().getMemberId() != null))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrackMember().getMemberId(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        Map<Long, Double> artistMappedByAmountPreviousMonthly = previousMonthlyTransactions.stream()
                .filter((transaction -> transaction.getTrackMember().getMemberId() != null))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrackMember().getMemberId(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));


        Map<Long, Double> sortedArtistMappedByAmount = artistMappedByAmount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map<Long, Double> sortedArtistMappedByAmountPreviousMonthly = artistMappedByAmountPreviousMonthly.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map.Entry<Long, Double> bestArtistInformation = sortedArtistMappedByAmount.entrySet().iterator().next();

        Member bestArtist = memberRepository.findById(bestArtistInformation.getKey()).orElseThrow(() -> {
            throw new MemberNotFoundException();
        });
        return BestArtistDto.builder()
                .memberId(bestArtist.getId())
                .enName(bestArtist.getEnName())
                .name(bestArtist.getName())
                .growthRate(
                        getGrowthRate(
                                sortedArtistMappedByAmountPreviousMonthly.get(bestArtistInformation.getKey()),
                                bestArtistInformation.getValue())
                )
                .build();
    }

    private Double getGrowthRate(Double previousMonthAmount, double amount) {
        if (previousMonthAmount == null) {
            return null;
        }
        double percentage = (amount - previousMonthAmount) / previousMonthAmount * 100;
        if (0 < percentage && percentage < 10) {
            return Math.floor(percentage * 10) / 10;
        }
        return Math.floor(percentage);
    }
}
