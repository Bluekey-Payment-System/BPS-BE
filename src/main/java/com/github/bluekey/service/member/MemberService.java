package com.github.bluekey.service.member;

import com.github.bluekey.dto.artist.ArtistAccountDto;
import com.github.bluekey.dto.request.admin.AdminArtistProfileRequestDto;
import com.github.bluekey.dto.request.artist.ArtistProfileRequestDto;
import com.github.bluekey.dto.response.artist.ArtistProfileResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.util.ImageUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ImageUploadUtil imageUploadUtil;

	@Transactional
	public ArtistProfileResponseDto updateArtistProfile(ArtistProfileRequestDto dto, MultipartFile file, Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);
		updateArtistEmail(dto.getEmail(), member);
		updateProfileImages(file, member);
		memberRepository.save(member);
		return ArtistProfileResponseDto.from(member);
	}

	@Transactional
	public ArtistAccountDto updateArtistProfileByAdmin(AdminArtistProfileRequestDto dto, Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);
		updateArtistName(dto, member);
		updateArtistCommissionRate(dto, member);
		memberRepository.save(member);
		return ArtistAccountDto.from(member);
	}

	private void updateArtistName(AdminArtistProfileRequestDto dto, Member member) {
		if (dto.getName() != null) {
			member.updateName(dto.getName());
		}
		if (dto.getEnName() != null) {
			member.updateEnName(dto.getEnName());
		}
	}

	private void updateArtistCommissionRate(AdminArtistProfileRequestDto dto, Member member) {
		if (dto.getCommissionRate() != null) {
			member.updateCommissionRate(Integer.valueOf(dto.getCommissionRate()));
		}
	}

	private void updateProfileImages(MultipartFile file, Member member) {
		if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
			return;
		}
		if (member.getProfileImage() != null) {
			imageUploadUtil.deleteImage(member.getProfileImage());
		}
		String profileImage = imageUploadUtil.uploadImage(file,
				imageUploadUtil.getProfileImageKey(file.getOriginalFilename(), member.getId()));
		member.updateProfileImage(profileImage);
	}

	private void updateArtistEmail(String email, Member member) {
		if (email == null) {
			return;
		}
		memberRepository.findMemberByEmailAndType(email, MemberType.USER)
				.ifPresent(m -> {
					throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
				});
		member.updateEmail(email);
	}
}
