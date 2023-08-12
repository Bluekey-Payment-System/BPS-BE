package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "월별 아티스트 매출액과 비율")
public class ArtistRevenueProportionDto {
	@Schema(description = "아티스트 기본 정보")
	private final MemberBaseDto artist;
	@Schema(description = "아티스트 매출액", example = "1000000")
	private final Long revenue;
	@Schema(description = "아티스트 매출액 증감률", example = "2.1")
	private final Float growthRate;
	@Schema(description = "아티스트 매출액 비율", example = "45")
	private final Double proportion;
}
