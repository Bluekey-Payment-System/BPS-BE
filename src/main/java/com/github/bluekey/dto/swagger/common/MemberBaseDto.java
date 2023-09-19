package com.github.bluekey.dto.swagger.common;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(description = "member의 기본 정보")
public class MemberBaseDto {
	@Schema(description = "고유 id", example = "1")
	private Long memberId;
	@Schema(description = "한글 이름", example = "아이유")
	private String name;
	@Schema(description = "영문 이름", example = "IU")
	private String enName;

	@Builder
	public MemberBaseDto(final Long memberId, final String name, final String enName) {
		this.memberId = memberId;
		this.name = name;
		this.enName = enName;
	}

	public static MemberBaseDto from(Member member) {
		return MemberBaseDto.builder()
				.memberId(member.getId())
				.name(member.getName())
				.enName(member.getEnName())
				.build();
	}
}
