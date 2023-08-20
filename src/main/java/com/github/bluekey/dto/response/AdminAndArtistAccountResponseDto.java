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
@Schema(description = "Super Admin이 관리하는 계정 리스트")
public class AdminAndArtistAccountResponseDto {
	@Schema(description = "아티스트 계정 리스트")
	private ArtistAccountResponseDto artistList;

	@Schema(description = "Admin 계정 리스트")
	private AdminAccountResponseDto adminList;

	@Builder
	public AdminAndArtistAccountResponseDto(final ArtistAccountResponseDto artistList,
			AdminAccountResponseDto adminList) {
		this.artistList = artistList;
		this.adminList = adminList;
	}
}
