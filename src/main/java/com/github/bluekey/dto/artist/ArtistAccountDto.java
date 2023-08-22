package com.github.bluekey.dto.artist;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "아티스트 계정 정보")
public class ArtistAccountDto {
	@Schema(description = "아티스트 이름(국문)", example = "혁기")
	private String name;
	@Schema(description = "아티스트 이름(영문)", example = "Hyukki")
	private String enName;
	@Schema(description = "로그인 아이디", example = "qwerty123")
	private String loginId;
	@Schema(description = "이메일", example = "bluekey@gmail.com")
	private String email;
	@Schema(description = "요율", example = "50")
	private Integer commissionRate;

	@Schema(description = "프로필 이미지", example = "artist.jpg")
	private String profileImage;

	@Builder
	private ArtistAccountDto(final String name, final String enName,
			final String loginId, final String email, final Integer commissionRate, final String profileImage) {
		this.name = name;
		this.enName = enName;
		this.loginId = loginId;
		this.email = email;
		this.commissionRate = commissionRate;
		this.profileImage = profileImage;
	}

	public static ArtistAccountDto from(Member member) {
		return ArtistAccountDto.builder()
				.name(member.getName())
				.enName(member.getEnName())
				.loginId(member.getLoginId())
				.email(member.getEmail().getValue())
				.commissionRate(member.getCommissionRate())
				.profileImage(member.getProfileImage())
				.build();
	}
}
