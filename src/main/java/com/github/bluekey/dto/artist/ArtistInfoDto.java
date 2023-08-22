package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistInfoDto {

    @Schema(description = "고유 id", example = "1")
    private final Long memberId;

    @Schema(description = "한글 이름", example = "김블루")
    private final String name;

    @Schema(description = "영문 이름", example = "bluekey")
    private final String enName;
}
