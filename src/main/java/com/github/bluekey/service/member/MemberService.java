package com.github.bluekey.service.member;

import com.github.bluekey.dto.request.SignupRequestDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public void createAdmin(SignupRequestDto dto) {
		if (memberRepository.findMemberByLoginId(dto.getLoginId()).isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_LOGIN_ID_VALUE);
		}
		if (memberRepository.findMemberByEmailAndType(dto.getEmail(), MemberType.ADMIN).isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
		}
		if (!validateAdminNickname(dto.getNickname())) {
			throw new BusinessException(ErrorCode.INVALID_NICKNAME_VALUE);
		}
		Member admin = Member.ByAdminBuilder()
				.loginId(dto.getLoginId())
				.password(dto.getPassword())
				.name(dto.getNickname())
				.email(dto.getEmail())
				.build();
		memberRepository.save(admin);
	}

	private boolean validateAdminNickname(String nickname) {
		// 아티스트의 활동 예명을 닉네임으로 사용할 수 없다.
		if (memberRepository.findMemberByNameAndType(nickname, MemberType.USER).isPresent() ||
				memberRepository.findMemberByEnNameAndType(nickname, MemberType.USER).isPresent()) {
			return false;
		}
		return true;
	}
}
