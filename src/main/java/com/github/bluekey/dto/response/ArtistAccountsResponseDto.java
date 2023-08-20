package com.github.bluekey.dto.response;

import com.github.bluekey.dto.ArtistAccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트 계정 리스트")
public class ArtistAccountsResponseDto {

	@Schema(description = "총 아티스트 계정 수")
	private Long totalItems;

	@Schema(description = "아티스트 계정 리스트")
	private List<ArtistAccountDto> contents = new ArrayList<>();

	@Builder
	public ArtistAccountsResponseDto(final Long totalItems, final List<ArtistAccountDto> contents) {
		this.totalItems = totalItems;
		this.contents = contents;
	}
}
