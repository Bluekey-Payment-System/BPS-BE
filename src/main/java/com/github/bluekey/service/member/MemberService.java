package com.github.bluekey.service.member;

import com.github.bluekey.dto.request.SignupRequestDto;
import com.github.bluekey.dto.response.SignupResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public SignupResponseDto createAdmin(SignupRequestDto dto) {
		validateSignUpRequest(dto);
		Member admin = Member.ByAdminBuilder()
				.loginId(dto.getLoginId())
				.password(dto.getPassword())
				.name(dto.getNickname())
				.email(dto.getEmail())
				.role(MemberRole.ADMIN) //TODO: 추후 PENDING 상태의 어드민 ROLE로 변경
				.build();
		Member newMember = memberRepository.save(admin);
		return SignupResponseDto.builder()
				.id(newMember.getId())
				.loginId(newMember.getLoginId())
				.nickname(newMember.getName())
				.email(newMember.getEmail().getValue())
				.password(newMember.getPassword().getValue())
				.build();
	}

	private void validateSignUpRequest(SignupRequestDto dto) {
		validateAdminLoginId(dto.getLoginId());
		validateAdminEmail(dto.getEmail());
		validateAdminNickname(dto.getNickname());
	}

	private void validateAdminLoginId(String loginId) {
		if (memberRepository.findMemberByLoginId(loginId).isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_LOGIN_ID_VALUE);
		}
	}

	private void validateAdminEmail(String email) {
		if (memberRepository.findMemberByEmailAndType(email, MemberType.ADMIN).isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
		}
	}

	private void validateAdminNickname(String nickname) {
		// 아티스트의 활동 예명을 닉네임으로 사용할 수 없다.
		if (memberRepository.findMemberByNameAndType(nickname, MemberType.USER).isPresent() ||
				memberRepository.findMemberByEnNameAndType(nickname, MemberType.USER).isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_NICKNAME_VALUE);
		}
	}
}
