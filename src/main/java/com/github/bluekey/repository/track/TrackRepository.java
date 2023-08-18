package com.github.bluekey.repository.track;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findTracksByNameAndAlbum(String name, Album album);
    List<Track> findTracksByEnNameAndAlbum(String enName, Album album);
}
