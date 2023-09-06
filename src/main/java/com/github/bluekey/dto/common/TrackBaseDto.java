package com.github.bluekey.dto.common;

import com.github.bluekey.entity.track.Track;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Track의 기본 정보")
public class TrackBaseDto {
	@Schema(description = "고유 id", example = "1")
	private Long trackId;
	@Schema(description = "한글 이름", example = "곡 제목")
	private String name;
	@Schema(description = "영문 이름", example = "track name")
	private String enName;

	@Builder
	public TrackBaseDto(final Long trackId, final String name, final String enName) {
		this.trackId = trackId;
		this.name = name;
		this.enName = enName;
	}

	public static TrackBaseDto from(Track track) {
		return TrackBaseDto.builder()
				.trackId(track.getId())
				.name(track.getName())
				.enName(track.getEnName())
				.build();
	}
}