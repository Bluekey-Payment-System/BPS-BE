package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistMonthlyAccountsDto {
    @Schema(description = "월", example = "7")
    private final int month;

    @Schema(description = "합", example = "1234124")
    private final long settlement;

    @Schema(description = "수익", example = "12341")
    private final long revenue;
}
