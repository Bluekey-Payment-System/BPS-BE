package com.github.bluekey.service.dashboard.v2;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.service.dashboard.v2.strategy.DashboardTransactionAllStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DashboardTransactionAdminClient implements DashboardTransactionAllStrategy<Transaction> {
    private final List<Transaction> transactions;

    public DashboardTransactionAdminClient(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Stream<Transaction> filteredByAlbumId(Long albumId) {
        return transactions.stream().filter(transaction -> transaction.getTrack().getAlbum().getId().equals(albumId));
    }

    @Override
    public Map<Track, Double> groupedByTrack() {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::getTrack,
                                Collectors.summingDouble(Transaction::getAmount)
                        ));
    }

    @Override
    public Map<TrackMember, Double> groupedByTrackMember() {
        return null;
    }

    @Override
    public Map<Album, Double> groupedByAlbum() {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                transaction -> transaction.getTrack().getAlbum(),
                                Collectors.summingDouble(Transaction::getAmount)
                        ));
    }

    @Override
    public Map<String, Double> groupedByMonth() {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::getDuration,
                                Collectors.summingDouble(Transaction::getAmount)
                        ));
    }

    @Override
    public Stream<Transaction> sort() {
        return null;
    }

    @Override
    public Stream<Transaction> sortByReverse() {
        return null;
    }
}
