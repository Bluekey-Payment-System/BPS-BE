package com.github.bluekey.dto.base.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistMonthlySettlementInfoDto {
    @Schema(description = "총정산액", example = "100000")
    private final double totalAmount;

    @Schema(description = "이익률", example = "10.2")
    private final double growthRate;
}
