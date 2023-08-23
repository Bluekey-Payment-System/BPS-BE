package com.github.bluekey.dto.artist;

import com.github.bluekey.dto.common.MemberBaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "월별 아티스트 매출액과 비율")
public class ArtistRevenueProportionDto {
	@Schema(description = "아티스트 기본 정보")
	private MemberBaseDto artist;

	@Schema(description = "아티스트 매출액", example = "1000000")
	private long revenue;

	@Schema(description = "아티스트 매출액 증감률", example = "2.1")
	private double growthRate;

	@Schema(description = "아티스트 매출액 비율", example = "45")
	private double proportion;

	@Builder
	public ArtistRevenueProportionDto(final MemberBaseDto artist, final long revenue, final double growthRate, final double proportion) {
		this.artist = artist;
		this.revenue = revenue;
		this.growthRate = growthRate;
		this.proportion = proportion;
	}
}