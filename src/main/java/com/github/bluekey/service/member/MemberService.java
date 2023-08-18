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
		// 중복 닉네임에 대한 검사를 ADMIN / USER를 나눠서 해야하나?
		if (memberRepository.findMemberByName(dto.getNickname()).isPresent()) {
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
}
