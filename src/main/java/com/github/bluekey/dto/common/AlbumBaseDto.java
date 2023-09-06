package com.github.bluekey.dto.common;

import com.github.bluekey.entity.album.Album;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Album의 기본 정보")
public class AlbumBaseDto {
	@Schema(description = "앨범ID", example = "1")
	private Long albumId;

	@Schema(description = "한글 이름", example = "앨범 이름")
	private String name;

	@Schema(description = "영문 이름", example = "Album name")
	private String enName;

	@Builder
	public AlbumBaseDto(final Long albumId, final String name, final String enName) {
		this.albumId = albumId;
		this.name = name;
		this.enName = enName;
	}

	public static AlbumBaseDto from(Album album) {
		return AlbumBaseDto.builder()
				.albumId(album.getId())
				.name(album.getName())
				.enName(album.getEnName())
				.build();
	}
}