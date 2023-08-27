package com.github.bluekey.service.track;

import com.github.bluekey.dto.request.track.TrackRequestDto;
import com.github.bluekey.dto.response.track.TrackResponseDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.track.TrackRepository;

public class TrackService {
    private TrackResponseDto trackResponseDto;
    private TrackRepository trackRepository;
    private AlbumRepository albumRepository;


    public TrackResponseDto insertTrack(Long albumId, TrackRequestDto dto) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(()-> new BusinessException(ErrorCode.INVALID_ALBUM_VALUE));

        Track track = trackRepository.save(dto.toTrack());

        trackRepository.save(track);

        return TrackResponseDto.from(track);
    }


    public TrackResponseDto updateTrack(Long trackId, TrackRequestDto dto) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(()-> new BusinessException(ErrorCode.INVALID_TRACK_VALUE));

        updateTrack(track, dto);

        trackRepository.save(track);

        return TrackResponseDto.from(track);
    }


    public Long deleteTrack(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(()-> new BusinessException(ErrorCode.INVALID_TRACK_VALUE));

        trackRepository.deleteById(trackId);

        return trackId;
    }


    private void updateTrack(Track track, TrackRequestDto dto) {
        if (dto.getName() != null) {
            track.updateName(dto.getName());
        }
        if (dto.getEnName() != null) {
            track.updateEnName(dto.getEnName());
        }

        track.updateIsOriginalTrack(dto.isOriginalTrack());

    }
}
