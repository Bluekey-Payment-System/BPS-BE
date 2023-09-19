package com.github.bluekey.dto.swagger.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIdResponseDto {
	@Schema(description = "회원 고유 id", example = "1")
	private Long memberId;

	@Builder
	public MemberIdResponseDto(final Long memberId) {
		this.memberId = memberId;
	}
}
