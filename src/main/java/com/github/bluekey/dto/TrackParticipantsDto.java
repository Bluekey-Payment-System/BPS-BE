package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackParticipantsDto {
    @Schema(description = "한글명", example = "김레드")
    private final String koName;

    @Schema(description = "영어명", example = "kimRed")
    private final String enName;

    @Schema(description = "요율", example = "15")
    private final int commissionRate;
}
