package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.album.AlbumTopDto;
import com.github.bluekey.dto.artist.ArtistRevenueProportionDto;
import com.github.bluekey.dto.common.MemberBaseDto;
import com.github.bluekey.dto.response.album.AlbumTopResponseDto;
import com.github.bluekey.dto.response.artist.ArtistTopResponseDto;
import com.github.bluekey.dto.response.artist.ArtistsRevenueProportionResponseDto;
import com.github.bluekey.dto.track.TrackBaseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import com.github.bluekey.service.album.AlbumService;
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
public class TopTrackDashBoardService {

    private static final String MONTH_PREFIX = "01";
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final AlbumService albumService;

    public ArtistsRevenueProportionResponseDto getTopArtists(String monthly, int rank) {
        String previousMonthly = getPreviousMonth(monthly);

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
        String previousMonthly = getPreviousMonth(monthly);
        Map<Track, Double> trackMappedByAmount = new HashMap<>();
        Map<Track, Double> trackMappedByAmountPreviousMonth = new HashMap<>();
        Map<Track, Double> sortedTrackMappedByAmount = new LinkedHashMap<>();
        Map<Track, Double> sortedTrackMappedByAmountPreviousMonth = new LinkedHashMap<>();
        List<AlbumTopDto> albums = new ArrayList<>();

        Double totalAmount = 0.0;
        Double totalProportion = 0.0;
        double totalEtcRevenue = 0.0;

        Member member = memberRepository.findById(memberId).orElseThrow(() -> {throw new MemberNotFoundException();});

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        if (member.getRole().equals(MemberRole.ARTIST)) {
            if (!albumService.isAlbumParticipant(albumId, memberId))
                throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
            trackMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter((transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()) &&
                            transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId)))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getTrack(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            trackMappedByAmountPreviousMonth = previousMonthlyTransactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter((transaction -> transaction.getTrackMember().getMemberId().equals(member.getId()) &&
                            transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId)))
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

            sortedTrackMappedByAmountPreviousMonth = trackMappedByAmountPreviousMonth.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        }

        if (member.getRole().equals(MemberRole.ADMIN) || member.getRole().equals(MemberRole.SUPER_ADMIN)) {
            trackMappedByAmount = transactions.stream()
//                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter((transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId)))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getTrack(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            trackMappedByAmountPreviousMonth = previousMonthlyTransactions.stream()
//                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                    .filter((transaction -> transaction.getTrackMember().getTrack().getAlbum().getId().equals(albumId)))
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

            sortedTrackMappedByAmountPreviousMonth = trackMappedByAmountPreviousMonth.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        }

        for(Map.Entry<Track, Double> entry : sortedTrackMappedByAmount.entrySet()) {
            totalAmount += entry.getValue();
        }

        for(Map.Entry<Track, Double> entry : sortedTrackMappedByAmount.entrySet()) {
            Track track = entry.getKey();
            Double amount = entry.getValue();

            Double previousMonthAmount = sortedTrackMappedByAmountPreviousMonth.get(entry.getKey());

            TrackBaseDto trackBaseDto = TrackBaseDto.builder()
                    .trackId(track.getId())
                    .name(track.getName())
                    .enName(track.getEnName())
                    .build();


            AlbumTopDto albumTopDto = AlbumTopDto.builder()
                    .track(trackBaseDto)
                    .revenue(getRevenue(amount))
                    .growthRate(getGrowthRate(previousMonthAmount, amount))
                    .proportion(getProportion(amount, totalAmount))
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
        String previousMonthly = getPreviousMonth(monthly);
        List<AlbumTopDto> albums = new ArrayList<>();

        Double totalAmount = 0.0;
        Double totalProportion = 0.0;
        double totalEtcRevenue = 0.0;

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthlyTransactions = transactionRepository.findTransactionsByDuration(previousMonthly);

        Map<Track, Double> trackMappedByAmount = transactions.stream()
                .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTrackMember().getTrack(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        Map<Track, Double> trackMappedByAmountPreviousMonth = previousMonthlyTransactions.stream()
                .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
                .filter(transaction -> transaction.getTrackMember().getMemberId().equals(memberId))
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

        Map<Track, Double> sortedTrackMappedByAmountPreviousMonth = trackMappedByAmountPreviousMonth.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        for(Map.Entry<Track, Double> entry : sortedTrackMappedByAmount.entrySet()) {
            totalAmount += entry.getValue();
        }

        for(Map.Entry<Track, Double> entry : sortedTrackMappedByAmount.entrySet()) {
            Track track = entry.getKey();
            Double amount = entry.getValue();

            Double previousMonthAmount = sortedTrackMappedByAmountPreviousMonth.get(entry.getKey());

            TrackBaseDto trackBaseDto = TrackBaseDto.builder()
                    .trackId(track.getId())
                    .name(track.getName())
                    .enName(track.getEnName())
                    .build();


            AlbumTopDto albumTopDto = AlbumTopDto.builder()
                    .track(trackBaseDto)
                    .revenue(getRevenue(amount))
                    .growthRate(getGrowthRate(previousMonthAmount, amount))
                    .proportion(getProportion(amount, totalAmount))
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
        if (previousMonthAmount == null || amount == 0.0) {
            return null;
        }

        if (previousMonthAmount == 0.0) {
            return null;
        }

        double percentage = (amount - previousMonthAmount) / previousMonthAmount * 100;
        if (0 < percentage && percentage < 10) {
            return Math.floor(percentage * 10) / 10;
        }
        return Math.floor(percentage);
    }

    private String getPreviousMonth(String monthly) {
        LocalDate date = LocalDate.parse(monthly + MONTH_PREFIX, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate previousMonth = date.minusMonths(1);
        return previousMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
