package com.github.bluekey.service.member;

import com.github.bluekey.dto.admin.AdminAccountDto;
import com.github.bluekey.dto.admin.AdminProfileUpdateDto;
import com.github.bluekey.dto.artist.ArtistAccountDto;
import com.github.bluekey.dto.request.admin.AdminArtistProfileRequestDto;
import com.github.bluekey.dto.request.artist.ArtistProfileRequestDto;
import com.github.bluekey.dto.response.admin.AdminAccountsResponseDto;
import com.github.bluekey.dto.response.admin.AdminProfileResponseDto;
import com.github.bluekey.dto.response.artist.ArtistAccountsResponseDto;
import com.github.bluekey.dto.response.artist.ArtistProfileResponseDto;
import com.github.bluekey.dto.response.artist.SimpleArtistAccountListResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.util.ImageUploadUtil;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	@Transactional
	public AdminProfileResponseDto updateAdminProfile(AdminProfileUpdateDto dto, MultipartFile file, Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(MemberNotFoundException::new);
		updateAdminEmail(dto.getEmail(), member);
		updateAdminNickname(dto.getNickname(), member);
		updateProfileImages(file, member);
		memberRepository.save(member);
		return AdminProfileResponseDto.from(member);
	}

	@Transactional(readOnly = true)
	public AdminAccountsResponseDto getAdminAccounts(PageRequest pageable) {
		Page<Member> adminList = memberRepository.findMembersByType(MemberType.ADMIN, pageable);
		return AdminAccountsResponseDto.builder()
				.totalItems(adminList.getTotalElements())
						.contents(adminList.getContent().stream()
								.map(AdminAccountDto::from).collect(Collectors.toList())).build();
	}

	@Transactional(readOnly = true)
	public ArtistAccountsResponseDto getArtistAccounts(PageRequest pageable) {

		Page<Member> artistList = memberRepository.findMembersByRole(MemberRole.ARTIST, pageable);
		return ArtistAccountsResponseDto.builder()
				.totalItems(artistList.getTotalElements())
				.contents(artistList.getContent().stream()
						.map(ArtistAccountDto::from).collect(Collectors.toList())).build();
	}

	@Transactional(readOnly = true)
	public SimpleArtistAccountListResponseDto getSimpleArtistAccounts() {
		List<Member> artists = memberRepository.findMemberByRoleAndIsRemovedFalse(MemberRole.ARTIST);
		return SimpleArtistAccountListResponseDto.from(artists);
	}

	public void validateAdminEmail(String email) {
		memberRepository.findMemberByEmailAndType(email, MemberType.ADMIN)
				.ifPresent(member -> {throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
				});
	}

	public void validateAdminNickname(String nickname) {
		// 아티스트의 활동 예명을 닉네임으로 사용할 수 없다.
		if (memberRepository.findMemberByNameAndType(nickname, MemberType.USER).isPresent() ||
				memberRepository.findMemberByEnNameAndType(nickname, MemberType.USER).isPresent()) {
			throw new BusinessException(ErrorCode.DUPLICATE_ARTIST_NAME);
		}
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

	private void updateAdminEmail(String email, Member member) {
		if (email != null) {
			validateAdminEmail(email);
			member.updateEmail(email);
		}
	}

	private void updateAdminNickname(String nickname, Member member) {
		if (nickname != null) {
			validateAdminNickname(nickname);
			member.updateName(nickname);
		}
	}
}
