package com.github.bluekey.dto.admin;

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
@Schema(description = "어드민 계정 정보")
public class AdminProfileViewDto {
	@Schema(description = "어드민 멤버 아이디", example = "1")
	private Long memberId;
	@Schema(description = "어드민 닉네임", example = "admin")
	private String nickname;
	@Schema(description = "로그인 아이디", example = "qwerty123")
	private String loginId;
	@Schema(description = "이메일", example = "bluekey@gmail.com")
	private String email;
	@Schema(description = "멤버 타입", example = "ADMIN")
	private MemberType type;
	@Schema(description = "멤버 권한", example = "SUPER_ADMIN")
	private MemberRole role;
	@Schema(description = "프로필 이미지", example = "https://bluekey.com/profile/1")
	private String profileImage;

	@Builder
	public AdminProfileViewDto(final Long memberId, final String nickname, final String loginId,
			final String email, final MemberType type, final MemberRole role, final String profileImage) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.loginId = loginId;
		this.email = email;
		this.type = type;
		this.role = role;
		this.profileImage = profileImage;
	}

	public static AdminProfileViewDto from(Member member) {
		return AdminProfileViewDto.builder()
				.memberId(member.getId())
			.nickname(member.getName())
			.loginId(member.getLoginId())
			.email(member.getEmail().getValue())
			.type(member.getType())
			.role(member.getRole())
			.profileImage(member.getProfileImage())
			.build();
	}
}
