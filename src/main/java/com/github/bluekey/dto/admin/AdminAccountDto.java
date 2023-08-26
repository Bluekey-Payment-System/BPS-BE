package com.github.bluekey.dto.admin;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "어드민 계정 정보")
public class AdminAccountDto {
	@Schema(description = "어드민 멤버 아이디", example = "1")
	private Long memberId;
	@Schema(description = "어드민 닉네임", example = "admin")
	private String nickname;
	@Schema(description = "로그인 아이디", example = "qwerty123")
	private String loginId;
	@Schema(description = "이메일", example = "bluekey@gmail.com")
	private String email;

	@Builder
	public AdminAccountDto(final Long memberId, final String nickname, final String loginId,
			final String email) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.loginId = loginId;
		this.email = email;
	}

	public static AdminAccountDto from(Member member) {
		return AdminAccountDto.builder()
				.memberId(member.getId())
			.nickname(member.getName())
			.loginId(member.getLoginId())
			.email(member.getEmail().getValue())
			.build();
	}
}
