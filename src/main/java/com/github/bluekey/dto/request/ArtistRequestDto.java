package com.github.bluekey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistRequestDto {
    @Schema(description = "이메일", example = "example@bluekey_domain.com")
    private final String email;

    @Schema(description = "로그인ID", example = "example@bluekey_domain.com")
    private final String loginId;

    @Schema(description = "한글명", example = "김블루")
    private final String name;

    @Schema(description = "영어명", example = "kimBlue")
    private final String enName;

    @Schema(description = "비밀번호", example = "test1234!")
    private final String password;

    @Schema(description = "영문과 동일 여부", example = "false")
    private final boolean isSameKoNameWithEnName;

    @Schema(description = "이미지", example = "https://s3...")
    private final String profileImage;

    @Schema(description = "기본요율", example = "40")
    private final double commissionRate;
}
