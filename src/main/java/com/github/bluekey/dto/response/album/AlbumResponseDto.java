package com.github.bluekey.dto.response.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범 정보")
public class AlbumResponseDto {
	@Schema(description = "앨범ID", example = "1")
	private Long albumId;

	@Schema(description = "한글명", example = "사랑")
	private String name;

	@Schema(description = "영어명", example = "love")
	private String enName;

	@Schema(description = "멤버 고유 ID", example = "1")
	private Long memberId;

	@Builder
	public AlbumResponseDto(final Long albumId, final String name, final String enName, final Long memberId) {
		this.albumId = albumId;
		this.name = name;
		this.enName = enName;
		this.memberId = memberId;
	}
}
