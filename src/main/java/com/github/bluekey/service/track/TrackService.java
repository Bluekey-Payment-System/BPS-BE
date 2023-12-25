package com.github.bluekey.service.track;

import com.github.bluekey.dto.swagger.request.track.TrackRequestDto;
import com.github.bluekey.dto.swagger.response.track.TrackResponseDto;
import com.github.bluekey.dto.swagger.track.TrackCommissionRateDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
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
    private final MemberRepository memberRepository;


    @Transactional
    public TrackResponseDto createTrack(Long albumId, TrackRequestDto dto) {
        Album album = albumRepository.findAlbumByIdAndIsRemovedFalseOrElseThrow(albumId);

        List<Track> tracks = trackRepository.findTracksByAlbumAndName(album, dto.getName());

        if(tracks.size() >= 1) {
            throw new BusinessException(ErrorCode.DUPLICATED_TRACK_NAME);
        }

        Track track = trackRepository.save(dto.toTrack(album));

        List<TrackCommissionRateDto> requestArtistsBefore = dto.getArtists();
        List<TrackCommissionRateDto> requestArtistsAfter = new ArrayList<>();

        for (TrackCommissionRateDto trackCommissionRateDto : requestArtistsBefore) {
            if (dto.getIsOriginalTrack()) {
                trackCommissionRateDto.updateCommissionRate(0);
            }
            requestArtistsAfter.add(trackCommissionRateDto);
        }


        for (TrackCommissionRateDto artist : requestArtistsAfter) {
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

    @Transactional
    public TrackResponseDto updateTrack(Long trackId, TrackRequestDto dto) {
        Track track = trackRepository.findByIdOrElseThrow(trackId);
        updateTrack(track, dto);
        trackRepository.save(track);

        return TrackResponseDto.from(track, track.getTrackMembers());
    }


    public Long removeTrack(Long trackId) {
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

        if (dto.getIsOriginalTrack() != null) {
            track.updateIsOriginalTrack(dto.getIsOriginalTrack());
        }

        if (dto.getArtists() != null) {
            List<TrackCommissionRateDto> trackCommissionRateDtos = dto.getArtists();
            List<TrackMember> trackMembers = new ArrayList<>();
            for (TrackCommissionRateDto trackCommissionRateDto : trackCommissionRateDtos) {
                Long memberId = trackCommissionRateDto.getMemberId();

                if (memberId != null) {
                    Member artist = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(memberId);
                    if (trackCommissionRateDto.getCommissionRate() == null) {
                        TrackMember trackMember = TrackMember.ByArtistBuilder()
                                .name(artist.getName())
                                .track(track)
                                .memberId(artist.getId())
                                .commissionRate(0)
                                .build();
                        trackMembers.add(trackMember);

                    } else {
                        TrackMember trackMember = TrackMember.ByArtistBuilder()
                                .name(artist.getName())
                                .track(track)
                                .memberId(artist.getId())
                                .commissionRate(trackCommissionRateDto.getCommissionRate())
                                .build();
                        trackMembers.add(trackMember);
                    }

                } else {
                    TrackMember trackMember = TrackMember.ByContractSingerBuilder()
                            .name(trackCommissionRateDto.getName())
                            .track(track)
                            .build();
                    trackMembers.add(trackMember);
                }
            }

            for (TrackMember trackMember : track.getTrackMembers()) {
                trackMemberRepository.deleteById(trackMember.getId());
            }

            track.updateTrackMembers(trackMembers);
        }
    }
}
