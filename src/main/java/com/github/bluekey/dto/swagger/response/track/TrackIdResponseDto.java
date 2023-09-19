package com.github.bluekey.dto.swagger.response.track;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrackIdResponseDto {
	@Schema(description = "트랙ID", example = "1")
	private Long trackId;

	@Builder
	public TrackIdResponseDto(final Long trackId) {
		this.trackId = trackId;
	}
}
