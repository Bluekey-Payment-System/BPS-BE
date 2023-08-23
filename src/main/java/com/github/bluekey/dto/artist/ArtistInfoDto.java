package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Artist 정보")
public class ArtistInfoDto {

    @Schema(description = "고유 id", example = "1")
    private Long memberId;

    @Schema(description = "한글 이름", example = "김블루")
    private String name;

    @Schema(description = "영문 이름", example = "bluekey")
    private String enName;

    @Builder
    public ArtistInfoDto(Long memberId, String name, String enName) {
        this.memberId = memberId;
        this.name = name;
        this.enName = enName;
    }
}
