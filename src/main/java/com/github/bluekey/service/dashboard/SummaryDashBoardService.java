package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.swagger.artist.ArtistMonthlyInfoDto;
import com.github.bluekey.dto.swagger.artist.ArtistMonthlySettlementInfoDto;
import com.github.bluekey.dto.swagger.artist.ArtistMonthlyTrackInfoDto;
import com.github.bluekey.dto.swagger.artist.BestArtistDto;
import com.github.bluekey.dto.swagger.common.TotalAndGrowthDto;
import com.github.bluekey.dto.swagger.response.album.AlbumSummaryResponseDto;
import com.github.bluekey.dto.swagger.response.artist.ArtistSummaryResponseDto;
import com.github.bluekey.dto.swagger.response.common.DashboardTotalInfoResponseDto;
import com.github.bluekey.dto.swagger.track.BestTrackDto;
import com.github.bluekey.entity.album.Album;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryDashBoardService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final AlbumService albumService;
    private final DashboardUtilService dashboardUtilService;

    @Transactional(readOnly = true)
    public DashboardTotalInfoResponseDto getAdminDashBoardSummaryInformation(String monthly, Long memberId) {
        BestArtistDto bestArtist = getBestArtist(monthly);
        double totalRevenue = 0.0;
        double totalPreviousMonthRevenue = 0.0;
        Double revenueGrowthRate;

        double netIncome;
        double previousMonthNetIncome;
        Double netIncomeGrowthRate;

        double settlementAmount;
        double previousMonthSettlementAmount;
        Double settlementAmountGrowthRate;

        Map<TrackMember, Double> trackMemberMappedByAmount = getTrackMemberMappedByAmount(monthly);
        Map<TrackMember, Double> previousMonthlyTrackMemberMappedByAmount = getPreviousMonthlyTrackMemberMappedByAmount(monthly);

        for (Map.Entry<TrackMember, Double> entry : trackMemberMappedByAmount.entrySet()) {
            totalRevenue += entry.getValue();
        }

        for (Map.Entry<TrackMember, Double> entry : previousMonthlyTrackMemberMappedByAmount.entrySet()) {
            totalPreviousMonthRevenue += entry.getValue();
        }
        revenueGrowthRate = dashboardUtilService.getGrowthRate(totalPreviousMonthRevenue, totalRevenue);

        settlementAmount = getSummarySettlementAmount(trackMemberMappedByAmount, memberId);
        previousMonthSettlementAmount = getSummarySettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);
        settlementAmountGrowthRate = dashboardUtilService.getGrowthRate(previousMonthSettlementAmount, settlementAmount);

        netIncome = getSettlementAmount(trackMemberMappedByAmount, memberId);
        previousMonthNetIncome = getSettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);

        netIncomeGrowthRate = dashboardUtilService.getGrowthRate(previousMonthNetIncome, netIncome);



        log.info("data = {} {} {} {} {} {}", totalRevenue, totalPreviousMonthRevenue, netIncome, previousMonthNetIncome, settlementAmount, previousMonthSettlementAmount);
        return DashboardTotalInfoResponseDto.builder()
                .bestArtist(bestArtist)
                .revenue(
                        TotalAndGrowthDto.builder()
                                .totalAmount((int) totalRevenue)
                                .growthRate(revenueGrowthRate)
                                .build()
                )
                .netIncome(
                        TotalAndGrowthDto.builder()
                                .totalAmount((int) settlementAmount)
                                .growthRate(settlementAmountGrowthRate)
                                .build()
                )
                .settlementAmount(
                        TotalAndGrowthDto.builder()
                                .totalAmount((int) netIncome)
                                .growthRate(netIncomeGrowthRate)
                                .build()
                )
                .build();
    }

    public ArtistSummaryResponseDto getArtistDashboardInformation(String monthly, Long memberId) {
        double settlementAmount = 0.0;
        double previousMonthSettlementAmount = 0.0;
        Double settlementAmountGrowthRate = 0.0;
        Member artist = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthTransactions = transactionRepository.findTransactionsByDuration(
                dashboardUtilService.getPreviousMonth(monthly));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Map<TrackMember, Double> trackMemberMappedByAmount = getArtistTrackMemberMappedByAmount(member, transactions);
        Map<TrackMember, Double> previousMonthlyTrackMemberMappedByAmount = getArtistTrackMemberMappedByAmount(member, previousMonthTransactions);

        settlementAmount = getSettlementAmount(trackMemberMappedByAmount, memberId);
        previousMonthSettlementAmount = getSettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);
        settlementAmountGrowthRate = dashboardUtilService.getGrowthRate(previousMonthSettlementAmount, settlementAmount);

        return ArtistSummaryResponseDto.builder()
                .memberId(artist.getId())
                .name(artist.getName())
                .enName(artist.getEnName())
                .bestAlbum(getTotalBestAlbum(monthly, member, transactions, previousMonthTransactions))
                .bestTrack(getTotalBestTrack(monthly, member, transactions, previousMonthTransactions))
                .settlementAmount(
                        ArtistMonthlySettlementInfoDto.builder()
                                .totalAmount(Math.floor(settlementAmount - (settlementAmount * 33 / 1000)))
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
        Double revenueGrowthRate = 0.0;

        double netIncome = 0.0;
        double previousMonthNetIncome = 0.0;
        Double netIncomeGrowthRate = 0.0;

        double settlementAmount = 0.0;
        double previousMonthSettlementAmount = 0.0;
        Double settlementAmountGrowthRate = 0.0;

        Map<TrackMember, Double> trackMemberMappedByAmount = getAlbumTrackMemberMappedByAmount(monthly, memberId, albumId);
        Map<TrackMember, Double> previousMonthlyTrackMemberMappedByAmount = getAlbumTrackMemberMappedByAmount(
                dashboardUtilService.getPreviousMonth(monthly), memberId, albumId);

        for (Map.Entry<TrackMember, Double> entry : trackMemberMappedByAmount.entrySet()) {
            totalRevenue += entry.getValue();
        }

        for (Map.Entry<TrackMember, Double> entry : previousMonthlyTrackMemberMappedByAmount.entrySet()) {
            totalPreviousMonthRevenue += entry.getValue();
        }

        revenueGrowthRate = dashboardUtilService.getGrowthRate(totalPreviousMonthRevenue, totalRevenue);
        // 추후 수정

        settlementAmount = getSummarySettlementAmount(trackMemberMappedByAmount, memberId);
        previousMonthSettlementAmount = getSummarySettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);
        settlementAmountGrowthRate = dashboardUtilService.getGrowthRate(previousMonthSettlementAmount, settlementAmount);

        netIncome = getAdminAlbumSettlementAmount(trackMemberMappedByAmount, memberId);;
        previousMonthNetIncome = getAdminAlbumSettlementAmount(previousMonthlyTrackMemberMappedByAmount, memberId);;
        netIncomeGrowthRate = dashboardUtilService.getGrowthRate(previousMonthNetIncome, netIncome);


        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        if (member.getRole().equals(MemberRole.ARTIST)) {
            if (!albumService.isAlbumParticipant(albumId, memberId))
                throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
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
                    .netIncome(
                            TotalAndGrowthDto.builder()
                                    .totalAmount(null)
                                    .growthRate(null)
                                    .build()
                    )
                    .settlementAmount(
                            TotalAndGrowthDto.builder()
                                    .totalAmount((int) settlementAmount)
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
                                    .totalAmount((int) totalRevenue)
                                    .growthRate(revenueGrowthRate)
                                    .build()
                    )
                    .netIncome(TotalAndGrowthDto.builder()
                            .totalAmount((int) settlementAmount)
                            .growthRate(settlementAmountGrowthRate)
                            .build()
                    )
                    .settlementAmount(
                            TotalAndGrowthDto.builder()
                                    .totalAmount((int) netIncome)
                                    .growthRate(netIncomeGrowthRate)
                                    .build()
                    )
                    .build();
        }

    }

//    private Double getIncome(Double revenue) {
//        return revenue - (revenue * 33 / 1000);
//    }

    private double getAdminAlbumSettlementAmount(Map<TrackMember, Double> trackMappedByAmount, Long memberId) {
        double totalSettlementAmount = 0.0;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        for (Map.Entry<TrackMember, Double> entry : trackMappedByAmount.entrySet()) {
            TrackMember trackMember = entry.getKey();
            Double amount = entry.getValue();
            if (member.getRole().equals(MemberRole.ARTIST)) {
//                 totalSettlementAmount += (amount * trackMember.getCommissionRate() / 100);
                totalSettlementAmount += dashboardUtilService.getArtistSettlement(amount, trackMember.getCommissionRate());
            } else {
//                totalSettlementAmount += amount - (amount * trackMember.getCommissionRate() / 100);
                totalSettlementAmount += dashboardUtilService.getCompanyNetIncome(amount, trackMember.getCommissionRate());
            }
        }
        return totalSettlementAmount;

    }

    private double getSettlementAmount(Map<TrackMember, Double> trackMemberMappedByAmount, Long memberId) {
        double totalSettlementAmount = 0.0;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));


        for (Map.Entry<TrackMember, Double> entry : trackMemberMappedByAmount.entrySet()) {
            TrackMember trackMember = entry.getKey();
            Double amount = entry.getValue();
            if (member.getRole().equals(MemberRole.ARTIST)) {
//                 totalSettlementAmount += (amount * trackMember.getCommissionRate() / 100);
                totalSettlementAmount += dashboardUtilService.getArtistSettlement(amount, trackMember.getCommissionRate());
            } else {
//                totalSettlementAmount += amount - (amount * trackMember.getCommissionRate() / 100);
                totalSettlementAmount += dashboardUtilService.getCompanyNetIncome(amount, trackMember.getCommissionRate());
            }
        }
        return totalSettlementAmount;
    }

    private double getSummarySettlementAmount(Map<TrackMember, Double> trackMemberMappedByAmount, Long memberId) {
        double totalSettlementAmount = 0.0;
        memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        for (Map.Entry<TrackMember, Double> entry : trackMemberMappedByAmount.entrySet()) {
            TrackMember trackMember = entry.getKey();
            Double amount = entry.getValue();
            Long trackMemberId = trackMember.getMemberId();
            if (trackMemberId != null) {
                Member trackMemberInformation = memberRepository.findById(trackMemberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
                if (trackMemberInformation.getRole().equals(MemberRole.ARTIST)) {
                    totalSettlementAmount += dashboardUtilService.getArtistSettlement(amount, trackMember.getCommissionRate());
                }
            }
        }
        return totalSettlementAmount;
    }

    private Map<TrackMember, Double> getArtistTrackMemberMappedByAmount(Member member, List<Transaction> transactions) {
        Map<TrackMember, Double> amountGroupedByTrackMember = dashboardUtilService.getAmountGroupedByTrackMemberFilteredByMemberId(transactions, member.getId());

        return dashboardUtilService.getSortedAmountGroupedByTrackMember(amountGroupedByTrackMember);
    }

    private Map<TrackMember, Double> getTrackMemberMappedByAmount(String monthly) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        Map<TrackMember, Double> amountGroupedByTrackMember = dashboardUtilService.getAmountGroupedByTrackMember(transactions);

        return dashboardUtilService.getSortedAmountGroupedByTrackMember(amountGroupedByTrackMember);
    }

    private Map<TrackMember, Double> getPreviousMonthlyTrackMemberMappedByAmount(String monthly) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(
                dashboardUtilService.getPreviousMonth(monthly));
        Map<TrackMember, Double> amountGroupedByTrackMember = dashboardUtilService.getAmountGroupedByTrackMember(transactions);

        return dashboardUtilService.getSortedAmountGroupedByTrackMember(amountGroupedByTrackMember);
    }

    private Map<TrackMember, Double> getAlbumTrackMemberMappedByAmount(String monthly, Long memberId, Long albumId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Map<TrackMember, Double> amountGroupByTrackMember = new LinkedHashMap<>();
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);

        if (member.getRole().equals(MemberRole.ARTIST)) {
            amountGroupByTrackMember = dashboardUtilService.getAmountGroupedByTrackMemberFilteredByMemberIdAndAlbumId(transactions, memberId, albumId);
        } else {
            amountGroupByTrackMember = dashboardUtilService.getAmountGroupedByTrackMemberFilteredByAlbumId(transactions, albumId);
        }

        return dashboardUtilService.getSortedAmountGroupedByTrackMember(amountGroupByTrackMember);
    }

    private ArtistMonthlyInfoDto getTotalBestAlbum(String monthly, Member member, List<Transaction> transactions, List<Transaction> previousMonthlyTransactions) {

        Map<Album, Double> amountGroupedByAlbum = dashboardUtilService
                .getAmountGroupedByAlbumMemberFilteredByMemberId(transactions, member.getId());

        Map<Album, Double> previousMonthlyAmountGroupedByAlbum = dashboardUtilService
                .getAmountGroupedByAlbumMemberFilteredByMemberId(previousMonthlyTransactions, member.getId());

        Map<Album, Double> sortedAmountGroupedByAlbum = dashboardUtilService.getSortedAmountGroupedByAlbum(amountGroupedByAlbum);

        Map<Album, Double> sortedPreviousMonthlyAmountGroupedByAlbum = dashboardUtilService.getSortedAmountGroupedByAlbum(previousMonthlyAmountGroupedByAlbum);

        Map.Entry<Album, Double> bestTrackInformation = sortedAmountGroupedByAlbum.entrySet()
                .stream()
                .findFirst()
                .orElse(null);
//        Album bestAlbum = albumRepository.findById(bestTrackInformation.getKey().getId()).orElseThrow(() -> {
//            throw new BusinessException(ErrorCode.ALBUM_NOT_FOUND);
//        });
        if(bestTrackInformation != null) {
            return ArtistMonthlyInfoDto.builder()
                    .albumId(bestTrackInformation.getKey().getId())
                    .enName(bestTrackInformation.getKey().getEnName())
                    .name(bestTrackInformation.getKey().getName())
                    .growthRate(
                            dashboardUtilService.getGrowthRate(
                                    sortedPreviousMonthlyAmountGroupedByAlbum.get(bestTrackInformation.getKey()),
                                    bestTrackInformation.getValue())
                    )
                    .build();
        } else {
            return ArtistMonthlyInfoDto.builder()
                    .albumId(null)
                    .enName(null)
                    .name(null)
                    .growthRate(null)
                    .build();
        }

    }

    private ArtistMonthlyTrackInfoDto getTotalBestTrack(String monthly, Member member, List<Transaction> transactions, List<Transaction> previousMonthlyTransactions) {
        Map<Track, Double> amountGroupedByTrack = dashboardUtilService.getAmountGroupedByTrackFilteredByMemberId(transactions, member.getId());

        Map<Track, Double> previousMonthlyAmountGroupedByTrack= dashboardUtilService.getAmountGroupedByTrackFilteredByMemberId(previousMonthlyTransactions, member.getId());

        Map<Track, Double> sortedAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(amountGroupedByTrack);

        Map<Track, Double> sortedPreviousMonthlyAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(previousMonthlyAmountGroupedByTrack);

        Map.Entry<Track, Double> bestTrackInformation = sortedAmountGroupedByTrack.entrySet()
                .stream()
                .findFirst()
                .orElse(null);
        if (bestTrackInformation != null) {
            return ArtistMonthlyTrackInfoDto.builder()
                    .trackId(bestTrackInformation.getKey().getId())
                    .enName(bestTrackInformation.getKey().getEnName())
                    .name(bestTrackInformation.getKey().getName())
                    .growthRate(
                            dashboardUtilService.getGrowthRate(
                                    sortedPreviousMonthlyAmountGroupedByTrack.get(bestTrackInformation.getKey()),
                                    bestTrackInformation.getValue())
                    )
                    .build();
        } else {
            return ArtistMonthlyTrackInfoDto.builder()
                    .trackId(null)
                    .enName(null)
                    .name(null)
                    .growthRate(null)
                    .build();
        }

    }


    private BestTrackDto getBestTrack(String monthly, Long memberId, Long albumId) {
        String previousMonthly = dashboardUtilService.getPreviousMonth(monthly);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Map<Track, Double> amountGroupedByTrack = new LinkedHashMap<>();
        Map<Track, Double> previousMonthlyAmountGroupedByTrack = new LinkedHashMap<>();
        Map<Track, Double> sortedAmountGroupedByTrack = new LinkedHashMap<>();
        Map<Track, Double> sortedPreviousMonthlyAmountGroupedByTrack = new LinkedHashMap<>();

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        if (member.getRole().equals(MemberRole.ARTIST)) {
            amountGroupedByTrack = dashboardUtilService.getAmountGroupedByTrackFilteredByMemberIdAndAlbumId(transactions, memberId, albumId);

            previousMonthlyAmountGroupedByTrack = dashboardUtilService.getAmountGroupedByTrackFilteredByMemberIdAndAlbumId(previousMonthlyTransactions, memberId, albumId);

            sortedAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(amountGroupedByTrack);

            sortedPreviousMonthlyAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(previousMonthlyAmountGroupedByTrack);
        } else {
            amountGroupedByTrack = dashboardUtilService.getAmountGroupedByTrackFilteredByAlbumId(transactions, albumId);

            previousMonthlyAmountGroupedByTrack = dashboardUtilService.getAmountGroupedByTrackFilteredByAlbumId(previousMonthlyTransactions, albumId);

            sortedAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(amountGroupedByTrack);

            sortedPreviousMonthlyAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(previousMonthlyAmountGroupedByTrack);
        }

        if (sortedAmountGroupedByTrack.isEmpty()) {
            return null;
        }

        Map.Entry<Track, Double> bestTrackInformation = sortedAmountGroupedByTrack.entrySet().iterator().next();
        return BestTrackDto.builder()
                .trackId(bestTrackInformation.getKey().getId())
                .enName(bestTrackInformation.getKey().getEnName())
                .name(bestTrackInformation.getKey().getName())
                .growthRate(
                        dashboardUtilService.getGrowthRate(
                                sortedPreviousMonthlyAmountGroupedByTrack.get(bestTrackInformation.getKey()),
                                bestTrackInformation.getValue())
                )
                .build();

    }

    private BestArtistDto getBestArtist(String monthly) {
        String previousMonthly = dashboardUtilService.getPreviousMonth(monthly);

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        Map<Long, Double> amountGroupedByMemberId = dashboardUtilService.getAmountGroupedByMemberId(transactions);

        Map<Long, Double> previousMonthlyAmountGroupedByMemberId = dashboardUtilService.getAmountGroupedByMemberId(previousMonthlyTransactions);

        Map<Long, Double> sortedAmountGroupedByMemberId = dashboardUtilService.getSortedAmountGroupedByMemberId(amountGroupedByMemberId);

        Map<Long, Double> sortedPreviousMonthlyAmountGroupedByMemberId = dashboardUtilService.getSortedAmountGroupedByMemberId(previousMonthlyAmountGroupedByMemberId);

        if (sortedAmountGroupedByMemberId.isEmpty()) {
            return null;
        }

        Map.Entry<Long, Double> bestArtistInformation = sortedAmountGroupedByMemberId.entrySet().iterator().next();

        Member bestArtist = memberRepository.findById(bestArtistInformation.getKey()).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return BestArtistDto.builder()
                .memberId(bestArtist.getId())
                .enName(bestArtist.getEnName())
                .name(bestArtist.getName())
                .growthRate(
                        dashboardUtilService.getGrowthRate(
                                sortedPreviousMonthlyAmountGroupedByMemberId.get(bestArtistInformation.getKey()),
                                bestArtistInformation.getValue())
                )
                .build();
    }
}
