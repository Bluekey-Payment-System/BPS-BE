package com.github.bluekey.dto;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "로그인 회원 정보")
public class LoginMemberDto {
	@Schema(description = "회원 Email", example = "bluekey@gmail.com")
	private final String email;
	@Schema(description = "회원 이름", example = "bluekey")
	private final String loginId;
	@Schema(description = "회원 타입", example = "ARTIST")
	private final MemberType type;
	@Schema(description = "회원 프로필 사진", example = "https://bluekey.com/profile.png")
	private final String profileImage;

	public static LoginMemberDto from(Member member) {
		return LoginMemberDto.builder()
				.loginId(member.getLoginId())
				.email(member.getEmail().getValue())
				.type(member.getType())
				.profileImage(member.getProfileImage())
				.build();
	}
}
