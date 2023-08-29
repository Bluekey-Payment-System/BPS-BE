package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.artist.*;
import com.github.bluekey.dto.common.TotalAndGrowthDto;
import com.github.bluekey.dto.response.album.AlbumSummaryResponseDto;
import com.github.bluekey.dto.response.artist.ArtistSummaryResponseDto;
import com.github.bluekey.dto.response.common.DashboardTotalInfoResponseDto;
import com.github.bluekey.dto.track.BestTrackDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryDashBoardService {
    private static final String MONTH_PREFIX = "01";
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;

    @Transactional(readOnly = true)
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
                                .build()
                )
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

    public ArtistSummaryResponseDto getArtistDashboardInformation(String monthly, Long memberId) {
        double settlementAmount = 0.0;
        double previousMonthSettlementAmount = 0.0;
        double settlementAmountGrowthRate = 0.0;
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthTransactions = transactionRepository.findTransactionsByDuration(getPreviousMonth(monthly));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {throw new MemberNotFoundException();});

        Map<TrackMember, Double> trackMemberMappedByAmount = getArtistTrackMemberMappedByAmount(member, transactions);
        Map<TrackMember, Double> previousMonthlyTrackMemberMappedByAmount = getArtistTrackMemberMappedByAmount(member, previousMonthTransactions);

        settlementAmount = getSettlementAmount(trackMemberMappedByAmount, memberId);
        previousMonthSettlementAmount = getSettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);
        settlementAmountGrowthRate = getGrowthRate(previousMonthSettlementAmount, settlementAmount);



        return ArtistSummaryResponseDto.builder()
                .bestAlbum(getTotalBestAlbum(monthly, member, transactions, previousMonthTransactions))
                .bestTrack(getTotalBestTrack(monthly, member, transactions, previousMonthTransactions))
                .settlementAmount(
                        ArtistMonthlySettlementInfoDto.builder()
                                .totalAmount(settlementAmount)
                                .growthRate(settlementAmountGrowthRate)
                                .build()
                )
                .build();
    }

    @Transactional(readOnly = true)
    public AlbumSummaryResponseDto getAlbumSummary(Long albumId, String monthly, Long memberId) {
        BestTrackDto bestTrack = getBestTrack(monthly, memberId, albumId);
        Album album = albumRepository.findById(albumId).orElseThrow(() -> {throw new BusinessException(ErrorCode.ALBUM_NOT_FOUND);});

        double totalRevenue = 0.0;
        double totalPreviousMonthRevenue = 0.0;
        double revenueGrowthRate = 0.0;

        double newIncome = 0.0;
        double previousMonthNewIncome = 0.0;
        double newIncomeGrowthRate = 0.0;

        double settlementAmount = 0.0;
        double previousMonthSettlementAmount = 0.0;
        double settlementAmountGrowthRate = 0.0;

        Map<TrackMember, Double> trackMemberMappedByAmount = getAlbumTrackMemberMappedByAmount(monthly, memberId, albumId);
        Map<TrackMember, Double> previousMonthlyTrackMemberMappedByAmount = getAlbumTrackMemberMappedByAmount(getPreviousMonth(monthly), memberId, albumId);

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

        settlementAmount = getAdminAlbumSettlementAmount(trackMemberMappedByAmount, memberId);
        previousMonthSettlementAmount = getAdminAlbumSettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);
        settlementAmountGrowthRate = getGrowthRate(previousMonthSettlementAmount, settlementAmount);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> {throw new MemberNotFoundException();});
        if (member.getRole().equals(MemberRole.ARTIST)) {
            return AlbumSummaryResponseDto.builder()
                    .albumId(album.getId())
                    .name(album.getName())
                    .enName(album.getEnName())
                    .bestTrack(bestTrack)
                    .revenue(
                            TotalAndGrowthDto.builder()
                            .totalAmount(null)
                            .growthRate(null)
                            .build()
                    )
                    .newIncome(
                            TotalAndGrowthDto.builder()
                                    .totalAmount(null)
                                    .growthRate(null)
                                    .build()
                    )
                    .settlementAmount(
                            TotalAndGrowthDto.builder()
                                    .totalAmount((long) settlementAmount)
                                    .growthRate(settlementAmountGrowthRate)
                                    .build()
                    )
                    .build();
        } else {
            return AlbumSummaryResponseDto.builder()
                    .albumId(album.getId())
                    .name(album.getName())
                    .enName(album.getEnName())
                    .bestTrack(bestTrack)
                    .revenue(
                            TotalAndGrowthDto.builder()
                                    .totalAmount((long) totalRevenue)
                                    .growthRate(revenueGrowthRate)
                                    .build()
                    )
                    .newIncome(TotalAndGrowthDto.builder()
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

    }

    private Double getIncome(Double revenue) {
        return revenue - (revenue * 33 / 1000);
    }

    private double getAdminAlbumSettlementAmount(Map<TrackMember, Double> trackMappedByAmount, Long memberId) {
        double totalSettlementAmount = 0.0;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException();
        });

        for (Map.Entry<TrackMember, Double> entry : trackMappedByAmount.entrySet()) {
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
        LocalDate date = LocalDate.parse(monthly + MONTH_PREFIX, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate previousMonth = date.minusMonths(1);
        return previousMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private Map<TrackMember, Double> getArtistTrackMemberMappedByAmount(Member member, List<Transaction> transactions) {
        Map<TrackMember, Double> trackMemberMappedByAmount = transactions.stream()
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
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

    private Map<TrackMember, Double> getAlbumTrackMemberMappedByAmount(String monthly, Long memberId, Long albumId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException();
        });
        Map<TrackMember, Double> trackMappedByAmount = new LinkedHashMap<>();
        Map<TrackMember, Double> sortedTrackMappedByAmount = new LinkedHashMap<>();
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);

        if (member.getRole().equals(MemberRole.ARTIST)) {
            trackMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
                    .filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
                    .collect(Collectors.groupingBy(
                            Transaction::getTrackMember,
                            Collectors.summingDouble(Transaction::getAmount)
                    ));
        } else {
            trackMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
                    .collect(Collectors.groupingBy(
                            Transaction::getTrackMember,
                            Collectors.summingDouble(Transaction::getAmount)
                    ));
        }

        sortedTrackMappedByAmount = trackMappedByAmount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        return sortedTrackMappedByAmount;
    }

    private ArtistMonthlyInfoDto getTotalBestAlbum(String monthly, Member member, List<Transaction> transactions, List<Transaction> previousMonthlyTransactions) {

        Map<Album, Double> trackMappedByAmount = transactions.stream()
                .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrackMember().getTrack().getAlbum(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        Map<Album, Double> trackMappedByAmountPreviousMonthly = previousMonthlyTransactions.stream()
                .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrackMember().getTrack().getAlbum(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));


        Map<Album, Double> sortedTrackMappedByAmount = trackMappedByAmount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map<Album, Double> sortedTrackMappedByAmountPreviousMonthly = trackMappedByAmountPreviousMonthly.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map.Entry<Album, Double> bestTrackInformation = sortedTrackMappedByAmount.entrySet().iterator().next();
        return ArtistMonthlyInfoDto.builder()
                .albumId(bestTrackInformation.getKey().getId())
                .enName(bestTrackInformation.getKey().getEnName())
                .name(bestTrackInformation.getKey().getName())
                .growthRate(
                        getGrowthRate(
                                sortedTrackMappedByAmountPreviousMonthly.get(bestTrackInformation.getKey()),
                                bestTrackInformation.getValue())
                )
                .build();
    }

    private ArtistMonthlyTrackInfoDto getTotalBestTrack(String monthly, Member member, List<Transaction> transactions, List<Transaction> previousMonthlyTransactions) {
        Map<Track, Double> trackMappedByAmount = transactions.stream()
                .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrackMember().getTrack(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        Map<Track, Double> trackMappedByAmountPreviousMonthly = previousMonthlyTransactions.stream()
                .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrackMember().getTrack(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));


        Map<Track, Double> sortedTrackMappedByAmount = trackMappedByAmount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map<Track, Double> sortedTrackMappedByAmountPreviousMonthly = trackMappedByAmountPreviousMonthly.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        Map.Entry<Track, Double> bestTrackInformation = sortedTrackMappedByAmount.entrySet().iterator().next();
        return ArtistMonthlyTrackInfoDto.builder()
                .trackId(bestTrackInformation.getKey().getId())
                .enName(bestTrackInformation.getKey().getEnName())
                .name(bestTrackInformation.getKey().getName())
                .growthRate(
                        getGrowthRate(
                                sortedTrackMappedByAmountPreviousMonthly.get(bestTrackInformation.getKey()),
                                bestTrackInformation.getValue())
                )
                .build();
    }


    private BestTrackDto getBestTrack(String monthly, Long memberId, Long albumId) {
        String previousMonthly = getPreviousMonth(monthly);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException();
        });
        Map<Track, Double> trackMappedByAmount = new LinkedHashMap<>();
        Map<Track, Double> trackMappedByAmountPreviousMonthly = new LinkedHashMap<>();
        Map<Track, Double> sortedTrackMappedByAmount = new LinkedHashMap<>();
        Map<Track, Double> sortedTrackMappedByAmountPreviousMonthly = new LinkedHashMap<>();

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        if (member.getRole().equals(MemberRole.ARTIST)) {
            trackMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
                    .filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getTrack(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            trackMappedByAmountPreviousMonthly = previousMonthlyTransactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter(transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()))
                    .filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getTrack(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));


            sortedTrackMappedByAmount = trackMappedByAmount.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            sortedTrackMappedByAmountPreviousMonthly = trackMappedByAmountPreviousMonthly.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        } else {
            trackMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getTrack(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            trackMappedByAmountPreviousMonthly = previousMonthlyTransactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter(transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getTrack(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));


            sortedTrackMappedByAmount = trackMappedByAmount.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            sortedTrackMappedByAmountPreviousMonthly = trackMappedByAmountPreviousMonthly.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        }

        if (sortedTrackMappedByAmount.isEmpty()) {
            return null;
        }

        Map.Entry<Track, Double> bestTrackInformation = sortedTrackMappedByAmount.entrySet().iterator().next();
        return BestTrackDto.builder()
                .trackId(bestTrackInformation.getKey().getId())
                .enName(bestTrackInformation.getKey().getEnName())
                .name(bestTrackInformation.getKey().getName())
                .growthRate(
                        getGrowthRate(
                                sortedTrackMappedByAmountPreviousMonthly.get(bestTrackInformation.getKey()),
                                bestTrackInformation.getValue())
                )
                .build();

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

        if (sortedArtistMappedByAmount.isEmpty()) {
            return null;
        }

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
