package com.github.bluekey.dto.artist;

import com.github.bluekey.dto.common.MemberBaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Best Artist 정보")
public class BestArtistDto extends MemberBaseDto {
	@Schema(description = "증감률", example = "0.3")
	private final Double growthRate;
	public BestArtistDto(Long id, String koName, String enName, Double growthRate) {
		super(id, koName, enName);
		this.growthRate = growthRate;
	}
}
