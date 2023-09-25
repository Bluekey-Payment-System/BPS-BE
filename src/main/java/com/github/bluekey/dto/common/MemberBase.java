package com.github.bluekey.dto.common;

import com.github.bluekey.entity.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBase {
	private Long memberId;
	private String name;
	private String enName;

	@Builder
	public MemberBase(final Long memberId, final String name, final String enName) {
		this.memberId = memberId;
		this.name = name;
		this.enName = enName;
	}

	public static MemberBase from(Member member) {
		return MemberBase.builder()
				.memberId(member.getId())
				.name(member.getName())
				.enName(member.getEnName())
				.build();
	}
}
