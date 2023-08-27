package com.github.bluekey.service.album;

import com.github.bluekey.dto.album.NewAlbumInfoDto;
import com.github.bluekey.dto.response.album.AlbumResponseDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.util.ImageUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AlbumService {

	private final MemberRepository memberRepository;
	private final ImageUploadUtil imageUploadUtil;

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
		String albumImage = null;
		if (file != null) {
			albumImage = imageUploadUtil.uploadImage(file,
					imageUploadUtil.getProfileImageKey(file.getOriginalFilename(), album.getId()));
		}
		album.updateProfileImage(albumImage);
		return AlbumResponseDto.from(album);
	}
}
