package com.github.bluekey.service.album;

import com.github.bluekey.dto.album.NewAlbumInfoDto;
import com.github.bluekey.dto.artist.ArtistInfoDto;
import com.github.bluekey.dto.common.MemberBaseDto;
import com.github.bluekey.dto.response.album.AlbumIdResponseDto;
import com.github.bluekey.dto.response.album.AlbumResponseDto;
import com.github.bluekey.dto.response.album.AlbumTrackListResponseDto;
import com.github.bluekey.dto.track.TrackArtistsDto;
import com.github.bluekey.dto.track.TrackInfoListDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import com.github.bluekey.s3.manager.S3PrefixType;
import com.github.bluekey.util.ImageUploadUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

	@Transactional
	public AlbumResponseDto createAlbum(MultipartFile file, NewAlbumInfoDto dto) {
		Member member = null;
		if (dto.getMemberId() != null) {
			member = memberRepository.findMemberByIdAndIsRemovedFalse(dto.getMemberId())
					.orElseThrow(MemberNotFoundException::new);
		}
		Album album = Album
				.ByAlbumWithRepresentativeArtistBuilder()
				.member(member)
				.name(dto.getName())
				.enName(dto.getEnName())
				.build();
		Album saveAlbum = albumRepository.save(album);
		updateAlbumImage(file, saveAlbum);
		return AlbumResponseDto.from(album);
	}

	@Transactional
	public AlbumIdResponseDto deleteAlbum(Long albumId) {
		Album album = albumRepository.findAlbumByIdAndIsRemovedFalse(albumId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

		album.remove();
		imageUploadUtil.deleteImage(album.getProfileImage());
		album.updateProfileImage(null);

		albumRepository.save(album);

		List<Track> tracks = trackRepository.findAllByAlbumId(albumId);
		tracks.forEach(track -> track.remove());

		return AlbumIdResponseDto.builder().albumId(albumId).build();
	}

	@Transactional(readOnly = true)
	public AlbumTrackListResponseDto getAlbumTrackList(Long albumId) {
		Album album = albumRepository.findAlbumByIdAndIsRemovedFalse(albumId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

		ArtistInfoDto artist = null;
		if (album.getArtistId() != null) {
			Member member = memberRepository.findMemberByIdAndIsRemovedFalse(album.getArtistId())
					.orElseThrow(MemberNotFoundException::new);
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
											.findMemberByIdAndIsRemovedFalse(member.getMemberId())
											.orElseThrow(MemberNotFoundException::new);
									return TrackArtistsDto.from(memberInfo);
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

	private void updateAlbumImage(MultipartFile file, Album album) {
		String albumImage = null;
		if (file != null && !file.isEmpty()) {
			albumImage = imageUploadUtil.uploadImage(file,
					imageUploadUtil.getAlbumImageKey(file.getOriginalFilename(), album.getId()));
		}
		album.updateProfileImage(albumImage);
		albumRepository.save(album);
	}
}
