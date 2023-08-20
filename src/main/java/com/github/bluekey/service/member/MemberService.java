package com.github.bluekey.service.member;

import com.github.bluekey.dto.ArtistAccountDto;
import com.github.bluekey.dto.request.ArtistRequestDto;
import com.github.bluekey.dto.request.SignupRequestDto;
import com.github.bluekey.dto.response.SignupResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.s3.manager.AwsS3Manager;
import com.github.bluekey.s3.manager.S3PrefixType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AwsS3Manager awsS3Manager;

	@Transactional
	public SignupResponseDto createAdmin(SignupRequestDto dto) {
		validateSignUpRequest(dto);
		Member admin = dto.toMember();
		Member newMember = memberRepository.save(admin);
		return SignupResponseDto.from(newMember);
	}

	@Transactional
	public ArtistAccountDto createArtist(MultipartFile file, ArtistRequestDto dto) {

		Member member = memberRepository.save(dto.toArtist());

		if (file.isEmpty()) {
			return ArtistAccountDto.from(member);
		}
		// S3 업로드 로직
		String fileUrl = awsS3Manager.upload(file, "profile/" + member.getId() + "/" + file.getOriginalFilename()+ "-" +member.getCreatedAt(),  S3PrefixType.IMAGE);
		member.updateProfileImage(fileUrl);
		memberRepository.save(member);
		return ArtistAccountDto.from(member);
	}
	private void validateSignUpRequest(SignupRequestDto dto) {
		validateAdminLoginId(dto.getLoginId());
		validateAdminEmail(dto.getEmail());
		validateAdminNickname(dto.getNickname());
	}

	private void validateAdminLoginId(String loginId) {
		memberRepository.findMemberByLoginId(loginId)
				.ifPresent(member -> {throw new BusinessException(ErrorCode.INVALID_LOGIN_ID_VALUE);
				});
	}

	private void validateAdminEmail(String email) {
		memberRepository.findMemberByEmailAndType(email, MemberType.ADMIN)
				.ifPresent(member -> {throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
				});
	}

	private void validateAdminNickname(String nickname) {
		// 아티스트의 활동 예명을 닉네임으로 사용할 수 없다.
		if (memberRepository.findMemberByNameAndType(nickname, MemberType.USER).isPresent() ||
				memberRepository.findMemberByEnNameAndType(nickname, MemberType.USER).isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_NICKNAME_VALUE);
		}
	}
}
