package com.github.bluekey.dto.track;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Best Track 정보")
public class BestTrackDto {

	@Schema(description = "고유 id", example = "1")
	private Long trackId;
	@Schema(description = "한글 이름", example = "곡 제목")
	private String name;
	@Schema(description = "영문 이름", example = "track name")
	private String enName;
	@Schema(description = "증감률", example = "0.3")
	private Double growthRate;

	@Builder
	public BestTrackDto(final Long trackId, final String name, final String enName, final Double growthRate) {
		this.trackId = trackId;
		this.name = name;
		this.enName = enName;
		this.growthRate = growthRate;
	}
}
