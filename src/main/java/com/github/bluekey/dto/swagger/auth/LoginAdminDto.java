package com.github.bluekey.dto.swagger.auth;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "로그인 어드민 정보")
public class LoginAdminDto {
	@Schema(description = "어드민 Id", example = "1")
	private Long memberId;
	@Schema(description = "어드민 닉네임", example = "bluekey")
	private String nickname;
	@Schema(description = "어드민 Email", example = "bluekey@gmail.com")
	private String email;
	@Schema(description = "어드민 이름", example = "bluekey")
	private String loginId;
	@Schema(description = "어드민 타입", example = "ADMIN")
	private MemberType type;
	@Schema(description ="어드민 역할", example = "SUPER_ADMIN")
	private MemberRole role;
	@Schema(description = "어드민 프로필 사진 url", example = "https://bluekey.com/profile.png")
	private String profileImage;

	@Builder
	public LoginAdminDto(final Long id, final String email, final String loginId, final String nickname,
			final MemberType type, final MemberRole role, final String profileImage) {
		this.memberId = id;
		this.nickname = nickname;
		this.email = email;
		this.loginId = loginId;
		this.type = type;
		this.role = role;
		this.profileImage = profileImage;
	}

	public static LoginAdminDto from(Member member) {
		return LoginAdminDto.builder()
				.id(member.getId())
				.nickname(member.getName())
				.loginId(member.getLoginId())
				.email(member.getEmail().getValue())
				.type(member.getType())
				.role(member.getRole())
				.profileImage(member.getProfileImage())
				.build();
	}
}
