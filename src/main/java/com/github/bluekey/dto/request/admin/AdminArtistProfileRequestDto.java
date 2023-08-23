package com.github.bluekey.dto.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "어드민이 아티스트 프로필 수정")
public class AdminArtistProfileRequestDto {
    @Schema(description = "아티스트한글명", example = "혁기")
    private String name;

    @Schema(description = "아티스트영어명", example = "hyunki")
    private String enName;

    @Schema(description = "아티스트기본요율", example = "40")
    private String commissionRate;

    @Builder
    public AdminArtistProfileRequestDto(final String name, final String enName, final String commissionRate) {
        this.name = name;
        this.enName = enName;
        this.commissionRate = commissionRate;
    }
}
