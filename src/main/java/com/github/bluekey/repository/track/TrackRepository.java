package com.github.bluekey.repository.track;

import com.github.bluekey.entity.track.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findTracksByName(String name);
    List<Track> findTracksByEnName(String enName);
}
