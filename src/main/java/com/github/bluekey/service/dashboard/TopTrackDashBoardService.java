package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.album.AlbumTopDto;
import com.github.bluekey.dto.artist.ArtistRevenueProportionDto;
import com.github.bluekey.dto.common.MemberBaseDto;
import com.github.bluekey.dto.response.album.AlbumTopResponseDto;
import com.github.bluekey.dto.response.artist.ArtistTopResponseDto;
import com.github.bluekey.dto.response.artist.ArtistsRevenueProportionResponseDto;
import com.github.bluekey.dto.common.TrackBaseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import com.github.bluekey.service.album.AlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopTrackDashBoardService {

    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final AlbumService albumService;
    private final DashboardUtilService dashboardUtilService;

    public ArtistsRevenueProportionResponseDto getTopArtists(String monthly, int rank) {
        String previousMonthly = dashboardUtilService.getPreviousMonth(monthly);

        Double totalAmount = 0.0;
        Double totalProportion = 0.0;
        double totalEtcRevenue = 0.0;
        List<ArtistRevenueProportionDto> artistRevenueProportions = new ArrayList<>();

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        Map<Long, Double> amountGroupedByMemberId = dashboardUtilService.getAmountGroupedByMemberId(transactions);

        Map<Long, Double> previousMonthlyAmountGroupedByMemberId = dashboardUtilService.getAmountGroupedByMemberId(previousMonthlyTransactions);

        Map<Long, Double> sortedAmountGroupedByMemberId = dashboardUtilService.getSortedAmountGroupedByMemberId(amountGroupedByMemberId);

        Map<Long, Double> sortedPreviousMonthlyAmountGroupedByMemberId = dashboardUtilService
                .getSortedAmountGroupedByMemberId(previousMonthlyAmountGroupedByMemberId);

        for(Map.Entry<Long, Double> entry : sortedAmountGroupedByMemberId.entrySet()) {
            totalAmount += entry.getValue();
        }

        for(Map.Entry<Long, Double> entry : sortedAmountGroupedByMemberId.entrySet()) {
            Long memberId = entry.getKey();
            Double amount = entry.getValue();

            Double previousMonthAmount = sortedPreviousMonthlyAmountGroupedByMemberId.get(entry.getKey());

            Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
            MemberBaseDto memberBaseDto = MemberBaseDto.from(member);

            ArtistRevenueProportionDto artistRevenueProportionDto = ArtistRevenueProportionDto.builder()
                    .artist(memberBaseDto)
                    .revenue(dashboardUtilService.getRevenue(amount))
                    .growthRate(dashboardUtilService.getGrowthRate(previousMonthAmount, amount))
                    .proportion(dashboardUtilService.getProportion(amount, totalAmount))
                    .build();
            artistRevenueProportions.add(artistRevenueProportionDto);
        }

        List<ArtistRevenueProportionDto> topArtistRevenueProportions = new ArrayList<>();
        List<ArtistRevenueProportionDto> etcArtistRevenueProportions = new ArrayList<>();
        if (artistRevenueProportions.size() > rank) {
            topArtistRevenueProportions = artistRevenueProportions.subList(0, rank);
            etcArtistRevenueProportions = artistRevenueProportions.subList(rank, artistRevenueProportions.size());

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
        } else {
            topArtistRevenueProportions = artistRevenueProportions.subList(0, artistRevenueProportions.size());
        }

        return ArtistsRevenueProportionResponseDto.from(topArtistRevenueProportions);

    }

    public AlbumTopResponseDto getTopTracks(Long albumId, String monthly, int rank, Long memberId) {
        String previousMonthly = dashboardUtilService.getPreviousMonth(monthly);
        Map<Track, Double> amountGroupedByTrack = new HashMap<>();
        Map<Track, Double> previousMonthAmountGroupedByTrack = new HashMap<>();
        Map<Track, Double> sortedAmountGroupedByTrack = new LinkedHashMap<>();
        Map<Track, Double> sortedPreviousMonthAmountGroupedByTrack = new LinkedHashMap<>();
        List<AlbumTopDto> albums = new ArrayList<>();

        Double totalAmount = 0.0;
        Double totalProportion = 0.0;
        double totalEtcRevenue = 0.0;

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        if (member.getRole().equals(MemberRole.ARTIST)) {
            if (!albumService.isAlbumParticipant(albumId, memberId))
                throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
            amountGroupedByTrack = dashboardUtilService
                    .getAmountGroupedByTrackFilteredByMemberIdAndAlbumId(transactions, memberId, albumId);

            previousMonthAmountGroupedByTrack = dashboardUtilService
                    .getAmountGroupedByTrackFilteredByMemberIdAndAlbumId(previousMonthlyTransactions, memberId, albumId);

            sortedAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(amountGroupedByTrack);

            sortedPreviousMonthAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(previousMonthAmountGroupedByTrack);
        }

        if (member.getRole().equals(MemberRole.ADMIN) || member.getRole().equals(MemberRole.SUPER_ADMIN)) {
            amountGroupedByTrack = dashboardUtilService.getAmountGroupedByTrackFilteredByAlbumId(transactions, albumId);

            previousMonthAmountGroupedByTrack = dashboardUtilService
                    .getAmountGroupedByTrackFilteredByAlbumId(previousMonthlyTransactions, albumId);
            sortedAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(amountGroupedByTrack);

            sortedPreviousMonthAmountGroupedByTrack = dashboardUtilService
                    .getSortedAmountGroupedByTrack(previousMonthAmountGroupedByTrack);
        }

        for(Map.Entry<Track, Double> entry : sortedAmountGroupedByTrack.entrySet()) {
            totalAmount += entry.getValue();
        }

        for(Map.Entry<Track, Double> entry : sortedAmountGroupedByTrack.entrySet()) {
            Track track = entry.getKey();
            Double amount = entry.getValue();

            Double previousMonthAmount = sortedPreviousMonthAmountGroupedByTrack.get(entry.getKey());

            TrackBaseDto trackBaseDto = TrackBaseDto.builder()
                    .trackId(track.getId())
                    .name(track.getName())
                    .enName(track.getEnName())
                    .build();


            AlbumTopDto albumTopDto = AlbumTopDto.builder()
                    .track(trackBaseDto)
                    .revenue(dashboardUtilService.getRevenue(amount))
                    .growthRate(dashboardUtilService.getGrowthRate(previousMonthAmount, amount))
                    .proportion(dashboardUtilService.getProportion(amount, totalAmount))
                    .build();

            albums.add(albumTopDto);
        }

        List<AlbumTopDto> topAlbums = new ArrayList<>();

        if (albums.size() > rank) {
            topAlbums = albums.subList(0, rank);
            List<AlbumTopDto> etcAlbums = albums.subList(rank, albums.size());
            for (AlbumTopDto albumTopDto : etcAlbums) {
                totalEtcRevenue += albumTopDto.getRevenue();
            }

            for (AlbumTopDto albumTopDto : topAlbums) {
                totalProportion += albumTopDto.getProportion();
            }

            AlbumTopDto etc = AlbumTopDto.builder()
                    .proportion(Math.floor((100.0 - totalProportion) * 10) / 10)
                    .track(null)
                    .growthRate(null)
                    .revenue(totalEtcRevenue)
                    .build();

            topAlbums.add(etc);

        } else {
            topAlbums = albums.subList(0, albums.size());

            for (AlbumTopDto albumTopDto : topAlbums) {
                totalProportion += albumTopDto.getProportion();
            }
        }

        return AlbumTopResponseDto.from(topAlbums);

    }
    public ArtistTopResponseDto getArtistTopTracks(String monthly, int rank, Long memberId) {
        String previousMonthly = dashboardUtilService.getPreviousMonth(monthly);
        List<AlbumTopDto> albums = new ArrayList<>();

        Double totalAmount = 0.0;
        Double totalProportion = 0.0;
        double totalEtcRevenue = 0.0;

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        Map<Track, Double> amountGroupedByTrack = dashboardUtilService.getAmountGroupedByTrackFilteredByMemberId(transactions, memberId);
        Map<Track, Double> previousMonthAmountGroupedByTrack = dashboardUtilService
                .getAmountGroupedByTrackFilteredByMemberId(previousMonthlyTransactions, memberId);

        Map<Track, Double> sortedAmountGroupedByTrack = dashboardUtilService.getSortedAmountGroupedByTrack(amountGroupedByTrack);

        Map<Track, Double> sortedAmountGroupedByTrackPreviousMonth = dashboardUtilService
                .getSortedAmountGroupedByTrack(previousMonthAmountGroupedByTrack);

        for(Map.Entry<Track, Double> entry : sortedAmountGroupedByTrack.entrySet()) {
            totalAmount += entry.getValue();
        }

        for(Map.Entry<Track, Double> entry : sortedAmountGroupedByTrack.entrySet()) {
            Track track = entry.getKey();
            Double amount = entry.getValue();

            Double previousMonthAmount = sortedAmountGroupedByTrackPreviousMonth.get(entry.getKey());

            TrackBaseDto trackBaseDto = TrackBaseDto.builder()
                    .trackId(track.getId())
                    .name(track.getName())
                    .enName(track.getEnName())
                    .build();


            AlbumTopDto albumTopDto = AlbumTopDto.builder()
                    .track(trackBaseDto)
                    .revenue(dashboardUtilService.getRevenue(amount))
                    .growthRate(dashboardUtilService.getGrowthRate(previousMonthAmount, amount))
                    .proportion(dashboardUtilService.getProportion(amount, totalAmount))
                    .build();

            albums.add(albumTopDto);
        }

        List<AlbumTopDto> topAlbums = new ArrayList<>();

        if (albums.size() > rank) {
            topAlbums = albums.subList(0, rank);
            List<AlbumTopDto> etcAlbums = albums.subList(rank, albums.size());
            for (AlbumTopDto albumTopDto : etcAlbums) {
                totalEtcRevenue += albumTopDto.getRevenue();
            }

            for (AlbumTopDto albumTopDto : topAlbums) {
                totalProportion += albumTopDto.getProportion();
            }

            AlbumTopDto etc = AlbumTopDto.builder()
                    .proportion(Math.floor((100.0 - totalProportion) * 10) / 10)
                    .track(null)
                    .growthRate(null)
                    .revenue(totalEtcRevenue)
                    .build();

            topAlbums.add(etc);

        } else {
            topAlbums = albums.subList(0, albums.size());

            for (AlbumTopDto albumTopDto : topAlbums) {
                totalProportion += albumTopDto.getProportion();
            }
        }

        return ArtistTopResponseDto.builder()
                .contents(topAlbums)
                .build();
    }
}
