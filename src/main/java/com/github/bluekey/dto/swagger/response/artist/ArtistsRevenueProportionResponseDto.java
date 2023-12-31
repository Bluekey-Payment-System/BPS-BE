package com.github.bluekey.dto.swagger.response.artist;

import com.github.bluekey.dto.swagger.artist.ArtistRevenueProportionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "월별 Top n 아티스트 매출액과 비율")
public class ArtistsRevenueProportionResponseDto {
	@Schema(description = "월별 Top n 아티스트들의 매출액과 비율 정보 리스트")
	private List<ArtistRevenueProportionDto> contents;

	@Builder
	public ArtistsRevenueProportionResponseDto(final List<ArtistRevenueProportionDto> contents) {
		this.contents = contents;
	}

	public static ArtistsRevenueProportionResponseDto from(List<ArtistRevenueProportionDto> contents) {
		return ArtistsRevenueProportionResponseDto.builder()
				.contents(contents)
				.build();
	}
}
