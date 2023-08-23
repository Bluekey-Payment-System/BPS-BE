package com.github.bluekey.dto.request.artist;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistRequestDto {
    @Schema(description = "이메일", example = "example@bluekey_domain.com")
    private String email;

    @Schema(description = "로그인ID", example = "example@bluekey_domain.com")
    private String loginId;

    @Schema(description = "한글명", example = "김블루")
    private String name;

    @Schema(description = "영어명", example = "kimBlue")
    private String enName;

    @Schema(description = "비밀번호", example = "test1234!")
    private String password;

    @Schema(description = "기본요율", example = "40")
    private Integer commissionRate;

    @Builder
    public ArtistRequestDto(String email, String loginId, String name, String enName, String password, Integer commissionRate) {
        this.email = email;
        this.loginId = loginId;
        this.name = name;
        this.enName = enName;
        this.password = password;
        this.commissionRate = commissionRate;
    }

    public Member toArtist() {
        return Member.ByArtistBuilder()
                .email(email)
                .loginId(loginId)
                .name(name)
                .enName(enName)
                .password(password)
                .commissionRate(commissionRate)
                .build();
    }
}
