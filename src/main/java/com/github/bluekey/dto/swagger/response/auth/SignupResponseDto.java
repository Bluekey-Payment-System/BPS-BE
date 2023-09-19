package com.github.bluekey.dto.swagger.response.auth;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원가입 응답")
public class SignupResponseDto {
	@Schema(description = "회원 고유 id", example = "1")
	private Long memberId;
	@Schema(description = "회원 Email", example = "bluekey1@gmail.com")
	private String email;
	@Schema(description = "회원 이름", example = "bluekey1")
	private String loginId;
	@Schema(description = "회원 닉네임", example = "블루키1")
	private String nickname;

	@Builder
	private SignupResponseDto(final Long id, final String email, final String loginId, final String nickname) {
		this.memberId = id;
		this.email = email;
		this.loginId = loginId;
		this.nickname = nickname;
	}

	public static SignupResponseDto from(Member member) {
		return SignupResponseDto.builder()
			.id(member.getId())
			.email(member.getEmail().getValue())
			.loginId(member.getLoginId())
			.nickname(member.getName())
			.build();
	}
}
