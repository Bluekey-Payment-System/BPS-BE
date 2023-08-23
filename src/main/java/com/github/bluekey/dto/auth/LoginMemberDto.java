package com.github.bluekey.dto.auth;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "로그인 회원 정보")
public class LoginMemberDto {
	@Schema(description = "회원 Id", example = "1")
	private Long memberId;
	@Schema(description = "회원 Email", example = "bluekey@gmail.com")
	private String email;
	@Schema(description = "회원 이름", example = "bluekey")
	private String loginId;
	@Schema(description = "회원 타입", example = "USER")
	private MemberType type;
	@Schema(description ="회원 역할", example = "ARTIST")
	private MemberRole role;
	@Schema(description = "회원 프로필 사진 url", example = "https://bluekey.com/profile.png")
	private String profileImage;

	@Builder
	public LoginMemberDto(final Long id, final String email, final String loginId, final MemberType type, final MemberRole role, final String profileImage) {
		this.memberId = id;
		this.email = email;
		this.loginId = loginId;
		this.type = type;
		this.role = role;
		this.profileImage = profileImage;
	}

	public static LoginMemberDto from(Member member) {
		return LoginMemberDto.builder()
				.id(member.getId())
				.loginId(member.getLoginId())
				.email(member.getEmail().getValue())
				.type(member.getType())
				.role(member.getRole())
				.profileImage(member.getProfileImage())
				.build();
	}
}
