package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "Album의 기본 정보")
public class AlbumBaseDto {
	@Schema(description = "앨범ID", example = "1")
	private final Long albumId;
	@Schema(description = "한글 이름", example = "앨범 이름")
	private final String koAlbumName;
	@Schema(description = "영문 이름", example = "Album name")
	private final String enAlbumName;

}