package com.github.bluekey.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
}
