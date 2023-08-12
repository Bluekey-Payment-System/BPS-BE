package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "공통적인 기본 정보")
public class CommonDto {
	@Schema(description = "고유 id", example = "1")
	private final Long id;
	@Schema(description = "한글 이름", example = "아이유")
	private final String koName;
	@Schema(description = "영문 이름", example = "IU")
	private final String enName;
}
