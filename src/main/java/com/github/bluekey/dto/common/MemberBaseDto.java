package com.github.bluekey.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "member의 기본 정보")
public class MemberBaseDto {
	@Schema(description = "고유 id", example = "1")
	private final Long id;
	@Schema(description = "한글 이름", example = "아이유")
	private final String koName;
	@Schema(description = "영문 이름", example = "IU")
	private final String enName;
}
