package com.github.bluekey.service.album;

import com.github.bluekey.dto.swagger.album.AlbumInfoDto;
import com.github.bluekey.dto.swagger.album.NewAlbumInfoDto;
import com.github.bluekey.dto.swagger.artist.ArtistInfoDto;
import com.github.bluekey.dto.swagger.response.album.AlbumIdResponseDto;
import com.github.bluekey.dto.swagger.response.album.AlbumResponseDto;
import com.github.bluekey.dto.swagger.response.album.AlbumTrackListResponseDto;
import com.github.bluekey.dto.swagger.response.artist.ArtistAlbumsListResponseDto;
import com.github.bluekey.dto.swagger.track.TrackArtistsDto;
import com.github.bluekey.dto.swagger.track.TrackInfoListDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import com.github.bluekey.util.ImageUploadUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final ImageUploadUtil imageUploadUtil;
    private final TrackMemberRepository trackMemberRepository;

    @Transactional
    public AlbumResponseDto createAlbum(MultipartFile file, NewAlbumInfoDto dto) {
        Album album;
        if (dto.getMemberId() != null) {
            Member member = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(dto.getMemberId());

            album = Album
                    .ByAlbumWithRepresentativeArtistBuilder()
                    .member(member)
                    .name(dto.getName())
                    .enName(dto.getEnName())
                    .build();
        } else {
            album = Album
                    .ByAlbumWithOutRepresentativeArtistBuilder()
                    .name(dto.getName())
                    .enName(dto.getEnName())
                    .build();
        }
        Album saveAlbum = albumRepository.save(album);
        createAlbumImage(file, saveAlbum);
        return AlbumResponseDto.from(saveAlbum);
    }

    @Transactional
    public AlbumIdResponseDto removeAlbum(Long albumId) {
        Album album = albumRepository.findAlbumByIdAndIsRemovedFalse(albumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        album.remove();
        imageUploadUtil.removeImage(album.getProfileImage());
        album.updateProfileImage(null);

        albumRepository.save(album);

        List<Track> tracks = trackRepository.findAllByAlbumId(albumId);
        tracks.forEach(track -> track.remove());

        return AlbumIdResponseDto.builder().albumId(albumId).build();
    }

    @Transactional
    public AlbumResponseDto updateAlbum(MultipartFile file, NewAlbumInfoDto dto, Long albumId) {
        Album album = albumRepository.findAlbumByIdAndIsRemovedFalse(albumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        if (dto != null) {
            updateAlbumProperties(album, dto);
        }

        updateAlbumImage(file, album);
        albumRepository.save(album);

        return AlbumResponseDto.from(album);
    }

    public void removeAlbumImage(Long albumId) {
        Album album = albumRepository.findAlbumByIdAndIsRemovedFalseOrElseThrow(albumId);
        if (album.getProfileImage() != null) {
            imageUploadUtil.removeImage(album.getProfileImage());
            album.updateProfileImage(null);
        }
        albumRepository.save(album);
    }

    @Transactional(readOnly = true)
    public AlbumTrackListResponseDto getAlbumTracks(Long albumId) {
        Album album = albumRepository.findAlbumByIdAndIsRemovedFalse(albumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        ArtistInfoDto artist = null;
        if (album.getArtistId() != null) {
            Member member = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(album.getArtistId());
            artist = ArtistInfoDto.from(member);
        }

        List<Track> tracks = trackRepository.findAllByAlbumIdAndIsRemovedFalse(albumId);

        List<TrackInfoListDto> trackInfoList = tracks
                .stream()
                .map(track -> {
                    List<TrackArtistsDto> memberList = track.getTrackMembers()
                            .stream()
                            .map(member -> {
                                if (member.getMemberId() != null) {
                                    Member memberInfo = memberRepository
                                            .findMemberByIdAndIsRemovedFalseOrElseThrow(member.getMemberId());

                                    return TrackArtistsDto.from(memberInfo, member);
                                } else {
                                    return TrackArtistsDto.from(member);
                                }
                            })
                            .collect(Collectors.toList());
                    return TrackInfoListDto.from(track, memberList);
                })
                .collect(Collectors.toList());

        return AlbumTrackListResponseDto
                .builder()
                .albumId(album.getId())
                .albumImage(album.getProfileImage())
                .name(album.getName())
                .enName(album.getEnName())
                .tracks(trackInfoList)
                .artist(artist)
                .build();
    }

    @Transactional(readOnly = true)
    public ArtistAlbumsListResponseDto getAlbums(Pageable pageable, Long memberId, String keyword) {
        Member member = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(memberId);

        Long totalItems = null;
        List<AlbumInfoDto> contents = new ArrayList<>();

        if (member.isAdmin()) {
            Page<Album> albums = albumRepository.findAllByIsRemovedFalseAndSearchByKeyword(pageable, keyword);

            totalItems = albums.getTotalElements();
            contents = albums.getContent().stream().map(AlbumInfoDto::from).collect(Collectors.toList());

        } else if (member.isUser()) {
            List<TrackMember> trackMembers = trackMemberRepository.findAllByMemberIdAndIsRemovedFalse(memberId);

            Set<Album> filteredAlbums = filterAlbumsByKeyword(trackMembers, keyword);

            totalItems = (long) filteredAlbums.size();

            List<Album> albums = filteredAlbums.stream().sorted(Comparator.comparing(Album::getCreatedAt).reversed()).collect(Collectors.toList());
            albums = paginateAlbums(albums, pageable);
            contents = albums.stream().map(AlbumInfoDto::from).collect(Collectors.toList());
        }
        return ArtistAlbumsListResponseDto
                .builder()
                .totalItems(totalItems)
                .contents(contents)
                .build();
    }

    @Transactional(readOnly = true)
    public ArtistAlbumsListResponseDto getAlbums(Pageable pageable, Long memberId) {
        Member member = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(memberId);
        Long totalItems = null;
        List<AlbumInfoDto> contents = new ArrayList<>();

        if (member.isAdmin()) {

            Page<Album> albums = albumRepository.findAllByIsRemovedFalseOrderByCreatedAtDesc(pageable);

            totalItems = albums.getTotalElements();
            contents = albums.getContent().stream().map(AlbumInfoDto::from).collect(Collectors.toList());

        } else if (member.isUser()) {

            List<TrackMember> trackMember = trackMemberRepository.findAllByMemberIdAndIsRemovedFalse(memberId);

            Set<Album> albums = trackMember.stream().map(TrackMember::getTrack).map(Track::getAlbum)
                    .collect(Collectors.toSet());

            totalItems = (long) albums.size();

            List<Album> sortedAlbums = albums.stream().sorted(Comparator.comparing(Album::getCreatedAt).reversed()).collect(Collectors.toList());
            sortedAlbums = paginateAlbums(sortedAlbums, pageable);
            contents = sortedAlbums.stream().map(AlbumInfoDto::from).collect(Collectors.toList());

        }
        return ArtistAlbumsListResponseDto
                .builder()
                .totalItems(totalItems)
                .contents(contents)
                .build();
    }

    @Transactional(readOnly = true)
    public boolean isAlbumParticipant(Long memberId, Long AlbumId) {
        Album album = albumRepository.findAlbumByIdAndIsRemovedFalse(AlbumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        long count = album.getTracks()
                .stream()
                .filter(track ->
                        track.getTrackMembers()
                                .stream()
                                .filter(TrackMember::isArtistTrack)
                                .anyMatch(trackMember -> trackMember.getMemberId().equals(memberId))).count();
        return count > 0;
    }

    private List<Album> paginateAlbums(List<Album> albums, Pageable pageable) {
        int end = (int) Math.min(pageable.getOffset() + pageable.getPageSize(), albums.size());
        if (albums.size() <= pageable.getOffset()) {
            return new ArrayList<>();
        } else {
            return albums.subList((int) pageable.getOffset(), end);
        }
    }

    private Set<Album> filterAlbumsByKeyword(List<TrackMember> trackMembers, String keyword) {
        return trackMembers.stream()
                .map(TrackMember::getTrack)
                .map(track -> getAlbumIfNameContainsKeyword(track, keyword))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Album getAlbumIfNameContainsKeyword(Track track, String keyword) {
        String albumName = track.getAlbum().getName();
        String albumEnName = track.getAlbum().getEnName();

        return (albumName != null && albumName.contains(keyword)) ||
                (albumEnName != null && albumEnName.contains(keyword)) ?
                track.getAlbum() : null;
    }

    private void createAlbumImage(MultipartFile file, Album album) {
        if (file != null && !file.isEmpty()) {
            String albumImagePath = imageUploadUtil.uploadImage(file,
                    imageUploadUtil.getAlbumImageKey(file.getOriginalFilename(), album.getId()));
            album.updateProfileImage(albumImagePath);
        }
        albumRepository.save(album);
    }

    private void updateAlbumProperties(Album album, NewAlbumInfoDto dto) {
        if (dto.getMemberId() != null) {
            memberRepository.findMemberByIdAndIsRemovedFalse(dto.getMemberId())
                    .orElseThrow(MemberNotFoundException::new);
            album.updateArtistId(dto.getMemberId());
        }
        if (dto.getName() != null) {
            album.updateName(dto.getName());
        }
        if (dto.getEnName() != null) {
            album.updateEnName(dto.getEnName());
        }
    }

    private void updateAlbumImage(MultipartFile file, Album album) {
        if (!file.isEmpty()) {
            if (album.getProfileImage() != null) {
                imageUploadUtil.removeImage(album.getProfileImage());
            }
            String albumImagePath = imageUploadUtil.uploadImage(file,
                    imageUploadUtil.getAlbumImageKey(file.getOriginalFilename(), album.getId()));
            album.updateProfileImage(albumImagePath);
        }
    }
}
