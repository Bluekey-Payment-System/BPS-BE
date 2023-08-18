package com.github.bluekey.repository.track;

import com.github.bluekey.entity.track.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
