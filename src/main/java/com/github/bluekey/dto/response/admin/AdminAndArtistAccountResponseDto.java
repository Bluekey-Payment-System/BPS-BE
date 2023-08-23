package com.github.bluekey.dto.response.admin;

import com.github.bluekey.dto.response.artist.ArtistAccountsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Super Admin이 관리하는 계정 리스트")
public class AdminAndArtistAccountResponseDto {
	@Schema(description = "아티스트 계정 리스트")
	private ArtistAccountsResponseDto artists;

	@Schema(description = "admin 계정 리스트")
	private AdminAccountsResponseDto admins;

	@Builder
	public AdminAndArtistAccountResponseDto(final ArtistAccountsResponseDto artists,
			AdminAccountsResponseDto admins) {
		this.artists = artists;
		this.admins= admins;
	}
}
