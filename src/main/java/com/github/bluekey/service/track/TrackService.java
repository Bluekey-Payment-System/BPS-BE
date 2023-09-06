package com.github.bluekey.service.track;

import com.github.bluekey.dto.request.track.TrackRequestDto;
import com.github.bluekey.dto.response.track.TrackResponseDto;
import com.github.bluekey.dto.track.TrackCommissionRateDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;

import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final TrackMemberRepository trackMemberRepository;


    @Transactional
    public TrackResponseDto insertTrack(Long albumId, TrackRequestDto dto) {
        Album album = albumRepository.findAlbumByIdAndIsRemovedFalseOrElseThrow(albumId);
        Track track = trackRepository.save(dto.toTrack(album));

        List<TrackCommissionRateDto> requestArtists = dto.getArtists();

        for (TrackCommissionRateDto artist : requestArtists) {
            if (artist.getMemberId() == null) {
                TrackMember trackMember = TrackMember.ByContractSingerBuilder()
                        .name(artist.getName())
                        .track(track)
                        .build();
                trackMemberRepository.save(trackMember);
            } else {
                TrackMember trackMember = TrackMember.ByArtistBuilder()
                        .memberId(artist.getMemberId())
                        .name(artist.getName())
                        .commissionRate(artist.getCommissionRate())
                        .track(track)
                        .build();
                trackMemberRepository.save(trackMember);
            }
        }

        trackRepository.save(track);
        List<TrackMember> trackMembers = trackMemberRepository.findTrackMembersByTrack(track);
        return TrackResponseDto.from(track, trackMembers);
    }

    public TrackResponseDto updateTrack(Long trackId, TrackRequestDto dto) {
        Track track = trackRepository.findByIdOrElseThrow(trackId);

        updateTrack(track, dto);

        trackRepository.save(track);
        List<TrackMember> trackMembers = new ArrayList<>();

        return TrackResponseDto.from(track, trackMembers);
    }


    public Long deleteTrack(Long trackId) {
        Track track = trackRepository.findByIdOrElseThrow(trackId);

        track.remove();
        trackRepository.save(track);

        return trackId;
    }


    private void updateTrack(Track track, TrackRequestDto dto) {
        if (dto.getName() != null) {
            track.updateName(dto.getName());
        }
        if (dto.getEnName() != null) {
            track.updateEnName(dto.getEnName());
        }

        track.updateIsOriginalTrack(dto.getIsOriginalTrack());
    }
}
