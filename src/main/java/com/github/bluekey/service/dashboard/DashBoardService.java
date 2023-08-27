package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.artist.ArtistRevenueProportionDto;
import com.github.bluekey.dto.common.MemberBaseDto;
import com.github.bluekey.dto.response.artist.ArtistsRevenueProportionResponseDto;
import com.github.bluekey.entity.member.Member;
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
public class DashBoardService {

    private static final String MONTH_PREFIX = "-01";
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    public ArtistsRevenueProportionResponseDto getTopArtists(String monthly, int rank) {
        LocalDate date = LocalDate.parse(monthly + MONTH_PREFIX, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate previousMonth = date.minusMonths(1);
        String previousMonthly = previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        Double totalAmount = 0.0;
        Double totalProportion = 0.0;
        double totalEtcRevenue = 0.0;
        List<ArtistRevenueProportionDto> artistRevenueProportions = new ArrayList<>();

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

        for(Map.Entry<Long, Double> entry : sortedArtistMappedByAmount.entrySet()) {
            totalAmount += entry.getValue();
        }

        for(Map.Entry<Long, Double> entry : sortedArtistMappedByAmount.entrySet()) {
            Long memberId = entry.getKey();
            Double amount = entry.getValue();

            Double previousMonthAmount = sortedArtistMappedByAmountPreviousMonthly.get(entry.getKey());

            Member member = memberRepository.findById(memberId).orElseThrow(() -> {throw new MemberNotFoundException();});
            MemberBaseDto memberBaseDto = MemberBaseDto.builder()
                    .memberId(member.getId())
                    .name(member.getName())
                    .enName(member.getEnName())
                    .build();

            ArtistRevenueProportionDto artistRevenueProportionDto = ArtistRevenueProportionDto.builder()
                    .artist(memberBaseDto)
                    .revenue(getRevenue(amount))
                    .growthRate(getGrowthRate(previousMonthAmount, amount))
                    .proportion(getProportion(amount, totalAmount))
                    .build();
            artistRevenueProportions.add(artistRevenueProportionDto);
        }

        List<ArtistRevenueProportionDto> topArtistRevenueProportions = artistRevenueProportions.subList(0, rank);
        List<ArtistRevenueProportionDto> etcArtistRevenueProportions = artistRevenueProportions.subList(rank, artistRevenueProportions.size());
        for (ArtistRevenueProportionDto artistRevenueProportionDto : topArtistRevenueProportions) {
            totalProportion += artistRevenueProportionDto.getProportion();
        }

        for (ArtistRevenueProportionDto artistRevenueProportionDto : etcArtistRevenueProportions) {
            totalEtcRevenue += artistRevenueProportionDto.getRevenue();
        }

        ArtistRevenueProportionDto etc = ArtistRevenueProportionDto.builder()
                .proportion(Math.floor((100.0 - totalProportion) * 10) / 10)
                .artist(null)
                .growthRate(null)
                .revenue(totalEtcRevenue)
                .build();

        topArtistRevenueProportions.add(etc);

        return ArtistsRevenueProportionResponseDto.from(topArtistRevenueProportions);
    }

    private double getProportion(double amount, double totalAmount) {
        double percentage = (amount / totalAmount) * 100;
        if (0 < percentage && percentage < 1) {
            return Math.floor(percentage * 10) / 10;
        }
        return Math.floor(percentage);
    }

    private int getRevenue(double revenue) {
        return (int) Math.floor(revenue);
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
