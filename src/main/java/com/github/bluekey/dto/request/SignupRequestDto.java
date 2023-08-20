package com.github.bluekey.dto.request;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원가입 요청")
public class SignupRequestDto {

	@Schema(description = "회원 Email", example = "bluekey1@gmail.com")
	private String email;
	@Schema(description = "회원 id", example = "bluekey1")
	private String loginId;
	@Schema(description = "회원 닉네임", example = "블루키1")
	private String nickname;
	@Schema(description = "회원 비밀번호", example = "blueblue123")
	private String password;

	@Builder
	private SignupRequestDto(String email, String loginId, String nickname, String password) {
		this.email = email;
		this.loginId = loginId;
		this.nickname = nickname;
		this.password = password;
	}

	public Member toMember() {
		return Member.ByAdminBuilder()
			.email(this.email)
			.loginId(this.loginId)
			.name(this.nickname)
			.password(this.password)
			.role(MemberRole.ADMIN) //TODO: 추후 PENDING 으로 변경
			.build();
	}
}
