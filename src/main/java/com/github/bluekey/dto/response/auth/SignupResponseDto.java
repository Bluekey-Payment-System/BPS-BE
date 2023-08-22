package com.github.bluekey.dto.response.auth;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원가입 응답")
public class SignupResponseDto {
	@Schema(description = "회원 고유 id", example = "1")
	private final Long id;
	@Schema(description = "회원 Email", example = "bluekey1@gmail.com")
	private final String email;
	@Schema(description = "회원 이름", example = "bluekey1")
	private final String loginId;
	@Schema(description = "회원 닉네임", example = "블루키1")
	private final String nickname;

	@Builder
	private SignupResponseDto(final Long id, final String email, final String loginId, final String nickname) {
		this.id = id;
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
