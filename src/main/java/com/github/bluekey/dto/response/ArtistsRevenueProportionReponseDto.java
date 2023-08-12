package com.github.bluekey.dto.response;

import com.github.bluekey.dto.ArtistRevenueProportionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "월별 Top n 아티스트 매출액과 비율")
public class ArtistsRevenueProportionReponseDto {
	private final List<ArtistRevenueProportionDto> contents;
}
