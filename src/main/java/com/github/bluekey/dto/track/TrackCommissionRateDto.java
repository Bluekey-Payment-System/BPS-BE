package com.github.bluekey.dto.track;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트 정보와 트랙 요율")
public class TrackCommissionRateDto {
    @Schema(description = "아티스트ID", example = "1")
    private Long memberId;

    @Schema(description = "아티스트", example = "1")
    private String name;

    @Schema(description = "요율", example = "50")
    private double commissionRate;

    @Builder
    public TrackCommissionRateDto(final Long memberId, final String name, final double commissionRate) {
        this.memberId = memberId;
        this.name = name;
        this.commissionRate = commissionRate;
    }
}
