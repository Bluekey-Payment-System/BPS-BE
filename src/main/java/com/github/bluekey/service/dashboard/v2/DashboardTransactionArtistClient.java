package com.github.bluekey.service.dashboard.v2;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.service.dashboard.v2.strategy.DashboardTransactionAllStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DashboardTransactionArtistClient implements DashboardTransactionAllStrategy<Transaction> {

    private final List<Transaction> transactions;
    private final Long memberId;

    public DashboardTransactionArtistClient(List<Transaction> transactions, Long memberId) {
        this.transactions = transactions;
        this.memberId = memberId;
    }

    @Override
    public Stream<Transaction> filteredByAlbumId(Long albumId) {
        return null;
    }

    @Override
    public Map<Track, Double> groupedByTrack() {
        return null;
    }

    @Override
    public Map<TrackMember, Double> groupedByTrackMember() {
        return null;
    }

    @Override
    public Map<Album, Double> groupedByAlbum() {
        return null;
    }

    @Override
    public Map<String, Double> groupedByMonth() {
        return null;
    }

    @Override
    public Stream<Transaction> sort() {
        return null;
    }

    @Override
    public Stream<Transaction> sortByReverse() {
        return null;
    }

    private boolean hasMemberIdInTrackMembers(Transaction transaction) {
        return transaction.getTrack().getTrackMembers().stream()
                .anyMatch(trackMember -> trackMember.getMemberId() != null && trackMember.getMemberId().equals(memberId));
    }
}
