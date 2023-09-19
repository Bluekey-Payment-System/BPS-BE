package com.github.bluekey.dto.swagger.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Best Artist 정보")
public class BestArtistDto {
	@Schema(description = "고유 id", example = "1")
	private Long memberId;
	@Schema(description = "한글 이름", example = "아이유")
	private String name;
	@Schema(description = "영문 이름", example = "IU")
	private String enName;
	@Schema(description = "증감률", example = "0.3")
	private Double growthRate;

	@Builder
	public BestArtistDto(final Long memberId, final String name, final String enName, final Double growthRate) {
		this.memberId = memberId;
		this.name = name;
		this.enName = enName;
		this.growthRate = growthRate;
	}
}
