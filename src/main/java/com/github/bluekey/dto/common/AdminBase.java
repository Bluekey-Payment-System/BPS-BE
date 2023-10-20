package com.github.bluekey.dto.common;

import com.github.bluekey.entity.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminBase {
	private Long memberId;
	private String loginId;
	private String nickname;

	@Builder
	public AdminBase(final Long memberId, final String loginId, final String nickname) {
		this.memberId = memberId;
		this.loginId = loginId;
		this.nickname = nickname;
	}

	public static AdminBase from(Member member) {
		return AdminBase.builder()
				.memberId(member.getId())
				.loginId(member.getLoginId())
				.nickname(member.getName())
				.build();
	}
}
