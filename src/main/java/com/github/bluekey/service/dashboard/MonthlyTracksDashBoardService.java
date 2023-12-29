package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.swagger.common.AlbumBaseDto;
import com.github.bluekey.dto.swagger.artist.ArtistMonthlyTrackListDto;
import com.github.bluekey.dto.swagger.common.MemberBaseDto;
import com.github.bluekey.dto.swagger.filter.MonthlyTrackFilter;
import com.github.bluekey.dto.swagger.response.artist.ArtistMonthlyTrackListResponseDto;
import com.github.bluekey.dto.swagger.response.track.TracksSettlementAmountResponseDto;
import com.github.bluekey.dto.swagger.common.TrackBaseDto;
import com.github.bluekey.dto.swagger.track.TrackSettlementAmountDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonthlyTracksDashBoardService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final DashboardUtilService dashboardUtilService;

    public ArtistMonthlyTrackListResponseDto getArtistTracks(String monthly, Pageable pageable, String sortBy, String searchType, String keyword, Long memberId, MonthlyTrackFilter monthlyTrackFilter) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<ArtistMonthlyTrackListDto> contents = new ArrayList<>();
        Comparator<ArtistMonthlyTrackListDto> comparator = getArtistSortComparator(sortBy);

        Map<Track, Double> trackMappedByAmount = transactions.stream()
                .filter(transaction -> transaction.getTrack().getTrackMembers().stream().anyMatch(trackMember -> trackMember.getMemberId() != null && trackMember.getMemberId().equals(memberId)))
                .filter(transaction -> {
                    if (keyword == null) {
                        return true;
                    }

                    String convertedKeyword = keyword.toLowerCase();
                    Track track = transaction.getTrack();
                    String trackName = track.getName().toLowerCase();
                    String trackEnName = track.getEnName().toLowerCase();

                    if (searchType.equals("trackName")) {
                        return trackName.contains(convertedKeyword) || trackEnName.contains(convertedKeyword);
                    } else if (searchType.equals("albumName")) {
                        Album album = track.getAlbum();
                        String albumName = album.getName().toLowerCase();
                        String albumEnName = album.getEnName().toLowerCase();
                        return albumName.contains(convertedKeyword) || albumEnName.contains(convertedKeyword);
                    }
                    return false;
                })
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrack(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        Map<Track, Double> sortedTrackMappedByAmount = dashboardUtilService.getSortedAmountGroupedByTrack(trackMappedByAmount);

        for (Map.Entry<Track, Double> entry : sortedTrackMappedByAmount.entrySet()) {
            Track track = entry.getKey();
            Album album = track.getAlbum();
            List<TrackMember> trackMembers = track.getTrackMembers();
            List<MemberBaseDto> artists = new ArrayList<>();
            Double amount = entry.getValue();

            TrackBaseDto trackBaseDto = TrackBaseDto.from(track);
            AlbumBaseDto albumBaseDto = AlbumBaseDto.from(album);
            Integer totalCommissionRate = 0;
            for (TrackMember trackMember : trackMembers) {
                Long id = trackMember.getMemberId();
                Optional<Member> member = memberRepository.findById(id);
                MemberBaseDto artistMonthlyArtistsDto;
                if (member.isPresent()) {
                    artistMonthlyArtistsDto = MemberBaseDto.from(member.get());
                    if (trackMember.getMemberId().equals(memberId)) {
                        totalCommissionRate = trackMember.getCommissionRate();
                    }
                } else {
                    artistMonthlyArtistsDto = MemberBaseDto.builder()
                            .name(trackMember.getName())
                            .build();
                }

                artists.add(artistMonthlyArtistsDto);
            }
            int revenue = dashboardUtilService.getRevenue(amount);
            int netIncome = dashboardUtilService.getCompanyNetIncome(amount, totalCommissionRate);
            int settlementAmount = dashboardUtilService.getArtistSettlement(amount, totalCommissionRate);
            int commissionRate = totalCommissionRate;

            ArtistMonthlyTrackListDto artistMonthlyTrackListDto = ArtistMonthlyTrackListDto.builder()
                    .track(trackBaseDto)
                    .album(albumBaseDto)
                    .artists(artists)
                    .settlementAmount(settlementAmount)
                    .revenue(revenue)
                    .netIncome(netIncome)
                    .commissionRate(commissionRate)
                    .build();
            if (hasArtistValidConditionInFilter(artistMonthlyTrackListDto, monthlyTrackFilter)) {
                if (commissionRate > 0) {
                    contents.add(artistMonthlyTrackListDto);
                }
            }
        }

        log.info("trackMappedByAmount = {}", sortedTrackMappedByAmount);
        contents.sort(comparator);
        return ArtistMonthlyTrackListResponseDto.builder()
                .totalItems(contents.size())
                .contents(DashboardUtilService.getPage(contents, pageable.getPageNumber(), pageable.getPageSize()))
                .build();
    }


    public TracksSettlementAmountResponseDto getAdminTracks(String monthly, Pageable pageable, String sortBy, String searchType, String keyword, MonthlyTrackFilter monthlyTrackFilter) {

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<TrackSettlementAmountDto> contents = new ArrayList<>();
        Comparator<TrackSettlementAmountDto> comparator = getAdminSortComparator(sortBy);

        Map<Track, Double> trackMappedByAmount = transactions.stream()
                .filter(transaction -> {
                    if (keyword == null) {
                        return true;
                    }

                    String convertedKeyword = keyword.toLowerCase();
                    Track track = transaction.getTrack();
                    String trackName = track.getName().toLowerCase();
                    String trackEnName = track.getEnName().toLowerCase();

                    if (searchType.equals("trackName")) {
                        return trackName.contains(convertedKeyword) || trackEnName.contains(
                                convertedKeyword);
                    } else if (searchType.equals("albumName")) {
                        Album album = track.getAlbum();
                        String albumName = album.getName().toLowerCase();
                        String albumEnName = album.getEnName().toLowerCase();
                        return albumName.contains(convertedKeyword) || albumEnName.contains(
                                convertedKeyword);
                    }
                    return false;
                })
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrack(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        Map<Track, Double> sortedTrackMappedByAmount = dashboardUtilService
                .getSortedAmountGroupedByTrack(trackMappedByAmount);

        for (Map.Entry<Track, Double> entry : sortedTrackMappedByAmount.entrySet()) {
            Track track = entry.getKey();
            Album album = track.getAlbum();
            List<TrackMember> trackMembers = track.getTrackMembers();
            List<MemberBaseDto> artists = new ArrayList<>();
            Double amount = entry.getValue();

            TrackBaseDto trackBaseDto = TrackBaseDto.from(track);
            AlbumBaseDto albumBaseDto = AlbumBaseDto.from(album);

            Integer totalCommissionRate = 0;

            for (TrackMember trackMember : trackMembers) {
                MemberBaseDto memberBaseDto;

                if (trackMember.getMemberId() != null) {
                    Long id = trackMember.getMemberId();
                    Optional<Member> member = memberRepository.findById(id);
                    if (member.isPresent()) {
                        memberBaseDto = MemberBaseDto.from(member.get());
                    } else {
                        memberBaseDto = MemberBaseDto.builder()
                                .name(trackMember.getName())
                                .build();
                    }
                } else {
                    memberBaseDto = MemberBaseDto.builder()
                            .name(trackMember.getName())
                            .build();
                }
                // TODO: 참여 아티스트가 2명 이상일 경우 이상한 값이 나오므로 조치 필요.
                totalCommissionRate += trackMember.getCommissionRate();
                artists.add(memberBaseDto);
            }
            int revenue = dashboardUtilService.getRevenue(amount);
            int netIncome = dashboardUtilService.getCompanyNetIncome(amount, totalCommissionRate);
            // MEMO: 원래 식이 어떤 settlement인지 잘 모르겠어서 임시로 주석 처리
//            int settlementAmount = getCalculateAmount(netIncome * ((100 - totalCommissionRate))) / 100;
            int settlementAmount = dashboardUtilService.getArtistSettlement(amount, totalCommissionRate);
            int commissionRate = 100 - totalCommissionRate;

            TrackSettlementAmountDto trackSettlementAmountDto = TrackSettlementAmountDto.builder()
                    .track(trackBaseDto)
                    .album(albumBaseDto)
                    .artists(artists)
                    .settlementAmount(settlementAmount)
                    .revenue(revenue)
                    .netIncome(netIncome)
                    .commissionRate(commissionRate)
                    .build();
            if (hasAdminValidConditionInFilter(trackSettlementAmountDto, monthlyTrackFilter)) {
                contents.add(trackSettlementAmountDto);
            }
        }
        contents.sort(comparator);
//        log.info("trackMappedByAmount = {}", sortedTrackMappedByAmount);

        return TracksSettlementAmountResponseDto.builder()
                .totalItems(contents.size())
                .contents(DashboardUtilService.getPage(contents, pageable.getPageNumber(), pageable.getPageSize()))
                .build();
    }

    private boolean hasAdminValidConditionInFilter(TrackSettlementAmountDto trackSettlementAmountDto, MonthlyTrackFilter monthlyTrackFilter) {
        if (monthlyTrackFilter.getMemberId() != null) {
            List<MemberBaseDto> artists = trackSettlementAmountDto.getArtists();
            boolean hasArtistInCondition = false;
            for (MemberBaseDto artist : artists) {
                if (artist.getMemberId() != null) {
                    if (artist.getMemberId().equals(monthlyTrackFilter.getMemberId())) {
                        hasArtistInCondition = true;
                    }
                }
            }

            if (!hasArtistInCondition) {
                return false;
            }
        }

        if (monthlyTrackFilter.getCommissionRateFrom() > trackSettlementAmountDto.getCommissionRate() ||
                trackSettlementAmountDto.getCommissionRate() > monthlyTrackFilter.getCommissionRateTo()
        ) {
            return false;
        }
        if(monthlyTrackFilter.getRevenueFrom() != null) {
            if(monthlyTrackFilter.getRevenueFrom() > trackSettlementAmountDto.getRevenue()) {
                return false;
            }
        }
        if (monthlyTrackFilter.getRevenueTo() != null) {
            if (trackSettlementAmountDto.getRevenue() > monthlyTrackFilter.getRevenueTo()) {
                return false;
            }
        }

        if(monthlyTrackFilter.getNetIncomeFrom() != null) {
            if(monthlyTrackFilter.getNetIncomeFrom() > trackSettlementAmountDto.getNetIncome()) {
                return false;
            }
        }

        if (monthlyTrackFilter.getNetIncomeTo() != null) {
            if (trackSettlementAmountDto.getNetIncome() > monthlyTrackFilter.getNetIncomeTo()) {
                return false;
            }
        }

        if (monthlyTrackFilter.getSettlementFrom() != null) {
            if (monthlyTrackFilter.getSettlementFrom() > trackSettlementAmountDto.getSettlementAmount()) {
                return false;
            }
        }

        if(monthlyTrackFilter.getSettlementTo() != null) {
            if (trackSettlementAmountDto.getSettlementAmount() > monthlyTrackFilter.getSettlementTo()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasArtistValidConditionInFilter(ArtistMonthlyTrackListDto artistMonthlyTrackListDto, MonthlyTrackFilter monthlyTrackFilter) {
        if (monthlyTrackFilter.getMemberId() != null) {
            List<MemberBaseDto> artists = artistMonthlyTrackListDto.getArtists();
            boolean hasArtistInCondition = false;
            for (MemberBaseDto artist : artists) {
                if (artist.getMemberId() != null) {
                    if (artist.getMemberId().equals(monthlyTrackFilter.getMemberId())) {
                        hasArtistInCondition = true;
                    }
                }
            }

            if (!hasArtistInCondition) {
                return false;
            }
        }

        if (monthlyTrackFilter.getCommissionRateFrom() > artistMonthlyTrackListDto.getCommissionRate() ||
                artistMonthlyTrackListDto.getCommissionRate() > monthlyTrackFilter.getCommissionRateTo()
        ) {
            return false;
        }
        if(monthlyTrackFilter.getRevenueFrom() != null) {
            if(monthlyTrackFilter.getRevenueFrom() > artistMonthlyTrackListDto.getRevenue()) {
                return false;
            }
        }
        if (monthlyTrackFilter.getRevenueTo() != null) {
            if (artistMonthlyTrackListDto.getRevenue() > monthlyTrackFilter.getRevenueTo()) {
                return false;
            }
        }

        if(monthlyTrackFilter.getNetIncomeFrom() != null) {
            if(monthlyTrackFilter.getNetIncomeFrom() > artistMonthlyTrackListDto.getNetIncome()) {
                return false;
            }
        }

        if (monthlyTrackFilter.getNetIncomeTo() != null) {
            if (artistMonthlyTrackListDto.getNetIncome() > monthlyTrackFilter.getNetIncomeTo()) {
                return false;
            }
        }

        if (monthlyTrackFilter.getSettlementFrom() != null) {
            if (monthlyTrackFilter.getSettlementFrom() > artistMonthlyTrackListDto.getSettlementAmount()) {
                return false;
            }
        }

        if(monthlyTrackFilter.getSettlementTo() != null) {
            if (artistMonthlyTrackListDto.getSettlementAmount() > monthlyTrackFilter.getSettlementTo()) {
                return false;
            }
        }
        return true;
    }

    private Comparator<ArtistMonthlyTrackListDto> getArtistSortComparator(String sortBy) {
        if (sortBy != null) {
            switch (sortBy) {
                case "revenue":
                    return Comparator.comparing(ArtistMonthlyTrackListDto::getRevenue).reversed();
                case "netIncome":
                    return Comparator.comparing(ArtistMonthlyTrackListDto::getNetIncome).reversed();
                case "settlement":
                    return Comparator.comparing(ArtistMonthlyTrackListDto::getSettlementAmount).reversed();
                case "commissionRate":
                    return Comparator.comparing(ArtistMonthlyTrackListDto::getCommissionRate).reversed();
            }
        }

        return Comparator.comparing(ArtistMonthlyTrackListDto::getRevenue).reversed();
    }

    private Comparator<TrackSettlementAmountDto> getAdminSortComparator(String sortBy) {
        if (sortBy != null) {
            switch (sortBy) {
                case "revenue":
                    return Comparator.comparing(TrackSettlementAmountDto::getRevenue).reversed();
                case "netIncome":
                    return Comparator.comparing(TrackSettlementAmountDto::getNetIncome).reversed();
                case "settlement":
                    return Comparator.comparing(TrackSettlementAmountDto::getSettlementAmount).reversed();
                case "commissionRate":
                    return Comparator.comparing(TrackSettlementAmountDto::getCommissionRate).reversed();
            }
        }

        return Comparator.comparing(TrackSettlementAmountDto::getRevenue).reversed();
    }
}
