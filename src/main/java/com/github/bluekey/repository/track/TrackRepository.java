package com.github.bluekey.repository.track;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findTrackByNameIgnoreCaseAndAlbum(String name, Album album);
    Optional<Track> findTrackByEnNameIgnoreCaseAndAlbum(String enName, Album album);

    Optional<Track> findTrackByNameIgnoreCase(String name);
    Optional<Track> findTrackByEnNameIgnoreCase(String enName);
}
