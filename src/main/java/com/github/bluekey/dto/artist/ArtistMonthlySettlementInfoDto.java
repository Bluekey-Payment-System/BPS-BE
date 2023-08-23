package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "아티스트 정산 정보")
public class ArtistMonthlySettlementInfoDto {
    @Schema(description = "총정산액", example = "100000")
    private Double totalAmount;

    @Schema(description = "이익률", example = "10.2")
    private Double growthRate;

    @Builder
    public ArtistMonthlySettlementInfoDto(final Double totalAmount, final Double growthRate) {
        this.totalAmount = totalAmount;
        this.growthRate = growthRate;
    }
}
