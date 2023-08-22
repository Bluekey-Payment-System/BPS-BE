package com.github.bluekey.dto.artist;

import com.github.bluekey.dto.common.MemberBaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Best Artist 정보")
public class BestArtistDto extends MemberBaseDto {
	@Schema(description = "증감률", example = "0.3")
	private Double growthRate;

	@Builder
	public BestArtistDto(Long memberId, String name, String enName, Double growthRate) {
		super(memberId, name, enName);
		this.growthRate = growthRate;
	}
}
