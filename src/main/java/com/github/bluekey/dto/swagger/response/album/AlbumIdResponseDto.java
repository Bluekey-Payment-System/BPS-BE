package com.github.bluekey.dto.swagger.response.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범 ID")
public class AlbumIdResponseDto {
	@Schema(description = "앨범ID", example = "1")
	private Long albumId;

	@Builder
	public AlbumIdResponseDto(final Long albumId) {
		this.albumId = albumId;
	}
}
