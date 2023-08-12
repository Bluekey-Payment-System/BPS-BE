package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "Album의 기본 정보")
public class AlbumBaseDto {
	@Schema(description = "고유 id", example = "1")
	private final Long id;
	@Schema(description = "한글 이름", example = "앨범 이름")
	private final String koName;
	@Schema(description = "영문 이름", example = "Album name")
	private final String enName;
}