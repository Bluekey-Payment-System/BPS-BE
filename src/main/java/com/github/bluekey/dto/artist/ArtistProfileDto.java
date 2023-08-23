package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트 프로필")
public class ArtistProfileDto {
    @Schema(description = "멤버 고유 ID", example = "1")
    private Long memberId;

    @Schema(description = "한글 이름", example = "김블루")
    private String name;

    @Schema(description = "영문 이름", example = "bluekey")
    private String enName;

    @Schema(description = "이미지", example = "https://s3...")
    private String profileImage;

    @Builder
    public ArtistProfileDto(final Long memberId, final String name, final String enName, final String profileImage) {
        this.memberId = memberId;
        this.name = name;
        this.enName = enName;
        this.profileImage = profileImage;
    }
}
