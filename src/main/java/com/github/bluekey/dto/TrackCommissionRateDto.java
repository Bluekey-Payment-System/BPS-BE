package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackCommissionRateDto {
    @Schema(description = "artist_ID")
    private final Long memberId;

    @Schema(description = "요율")
    private final int commissionRate;
}
