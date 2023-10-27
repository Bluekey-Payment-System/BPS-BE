package com.github.bluekey.repository.track;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import java.util.List;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findTrackByNameIgnoreCaseAndAlbum(String name, Album album);
    Optional<Track> findTrackByEnNameIgnoreCaseAndAlbum(String enName, Album album);
    List<Track> findTrackByNameIgnoreCaseOrEnNameIgnoreCase(String name, String enName);
    Optional<Track> findTrackByNameIgnoreCase(String name);
    Optional<Track> findTrackByEnNameIgnoreCase(String enName);

    List<Track> findAllByAlbumId(Long albumId);
    List<Track> findAllByAlbumIdAndIsRemovedFalse(Long albumId);
    List<Track> findAllByName(String name);
    List<Track> findAllByEnName(String enName);

    default Track findByIdOrElseThrow(Long id) {
        return this.findById(id).orElseThrow(() ->
                new BusinessException(ErrorCode.TRACK_NOT_FOUND)
        );
    }
}
