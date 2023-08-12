package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "Track의 기본 정보")
public class TrackBaseDto {
	@Schema(description = "고유 id", example = "1")
	private final Long id;
	@Schema(description = "한글 이름", example = "곡 제목")
	private final String koName;
	@Schema(description = "영문 이름", example = "track name")
	private final String enName;
}