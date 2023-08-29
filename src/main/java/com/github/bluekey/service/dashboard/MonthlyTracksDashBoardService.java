package com.github.bluekey.service.dashboard;

import com.github.bluekey.dto.album.AlbumBaseDto;
import com.github.bluekey.dto.common.MemberBaseDto;
import com.github.bluekey.dto.response.track.TracksSettlementAmountResponseDto;
import com.github.bluekey.dto.track.TrackBaseDto;
import com.github.bluekey.dto.track.TrackSettlementAmountDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
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

    private final TrackMemberRepository trackMemberRepository;
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;


    public TracksSettlementAmountResponseDto getAdminTracks(String monthly, Pageable pageable, String searchType, String keyword) {

        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<TrackSettlementAmountDto> contents = new ArrayList<>();

        Map<Track, Double> trackMappedByAmount = transactions.stream()
                .filter(transaction -> {
                    if (keyword == null) {
                        return true;
                    }

                    String convertedKeyword = keyword.toLowerCase();
                    Track track = transaction.getTrackMember().getTrack();
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

        for (Map.Entry<Track, Double> entry : sortedTrackMappedByAmount.entrySet()) {
            Track track = entry.getKey();
            Album album = track.getAlbum();
            List<TrackMember> trackMembers = track.getTrackMembers();
            List<MemberBaseDto> artists = new ArrayList<>();
            Double amount = entry.getValue();

            TrackBaseDto trackBaseDto = TrackBaseDto.builder()
                    .trackId(track.getId())
                    .name(track.getName())
                    .enName(track.getEnName())
                    .build();
            AlbumBaseDto albumBaseDto = AlbumBaseDto.builder()
                    .albumId(album.getId())
                    .name(album.getName())
                    .enName(album.getEnName())
                    .build();

            Integer totalCommissionRate = 0;

            for (TrackMember trackMember : trackMembers) {
                Long id = trackMember.getMemberId();
                Optional<Member> member = memberRepository.findById(id);
                MemberBaseDto memberBaseDto;
                if (member.isPresent()) {
                    memberBaseDto = MemberBaseDto.from(member.get());
                } else {
                    memberBaseDto = MemberBaseDto.builder()
                            .name(trackMember.getName())
                            .build();
                }
                totalCommissionRate += trackMember.getCommissionRate();
                artists.add(memberBaseDto);
            }
            int revenue = getCalculateAmount(amount);
            int newIncome = getCalculateAmount(amount - (amount * 33 / 1000));
            int settlementAmount = getCalculateAmount(newIncome * ((100 - totalCommissionRate))) / 100;
            int commissionRate = 100 - totalCommissionRate;

            TrackSettlementAmountDto trackSettlementAmountDto = TrackSettlementAmountDto.builder()
                    .track(trackBaseDto)
                    .album(albumBaseDto)
                    .artists(artists)
                    .settlementAmount(settlementAmount)
                    .revenue(revenue)
                    .newIncome(newIncome)
                    .commissionRate(commissionRate)
                    .build();

            contents.add(trackSettlementAmountDto);
        }

        log.info("trackMappedByAmount = {}", sortedTrackMappedByAmount);

        return TracksSettlementAmountResponseDto.builder()
                .totalItems(contents.size())
                .contents(getPage(contents, pageable.getPageNumber(), pageable.getPageSize()))
                .build();
    }

    private int getCalculateAmount(double revenue) {
        return (int) Math.ceil(revenue);
    }

    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page) * pageSize;
        if (sourceList == null || sourceList.size() <= fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }
}
