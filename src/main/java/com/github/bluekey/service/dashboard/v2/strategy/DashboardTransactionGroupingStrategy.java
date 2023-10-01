package com.github.bluekey.service.dashboard.v2.strategy;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;

import java.util.Map;

public interface DashboardTransactionGroupingStrategy {
    Map<Track, Double>  groupedByTrack();
    Map<TrackMember, Double> groupedByTrackMember();
    Map<Album, Double> groupedByAlbum();

    Map<String, Double> groupedByMonth();
}
