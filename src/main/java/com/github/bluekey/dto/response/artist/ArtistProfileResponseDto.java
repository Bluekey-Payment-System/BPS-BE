package com.github.bluekey.dto.response.artist;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트 프로필 응답")
public class ArtistProfileResponseDto {
	@Schema(description = "멤버 아이디", example = "1")
	private Long memberId;
	@Schema(description = "멤버 권한", example = "ARTIST")
	private MemberRole role;
	@Schema(description = "멤버 타입", example = "USER")
	private MemberType type;
	@Schema(description = "이메일", example = "example@bluekey_domain.com")
	private String email;
	@Schema(description = "로그인 id", example = "dfjalke")
	private String loginId;
	@Schema(description = "한글이름", example = "블루키")
	private String name;
	@Schema(description = "영문이름", example = "bluekey")
	private String enName;
	@Schema(description = "프로필 이미지", example = "https://s3...")
	private String profileImage;

	@Builder
	public ArtistProfileResponseDto(final Long memberId, final MemberRole role,
			final MemberType type, final String email, final String loginId,
			final String name, final String enName, final String profileImage) {
		this.memberId = memberId;
		this.role = role;
		this.type = type;
		this.email = email;
		this.loginId = loginId;
		this.name = name;
		this.enName = enName;
		this.profileImage = profileImage;
	}

	public static ArtistProfileResponseDto from(Member member) {
		return ArtistProfileResponseDto.builder()
				.memberId(member.getId())
				.role(member.getRole())
				.type(member.getType())
				.email(member.getEmail().getValue())
				.loginId(member.getLoginId())
				.name(member.getName())
				.enName(member.getEnName())
				.profileImage(member.getProfileImage())
				.build();
	}
}
