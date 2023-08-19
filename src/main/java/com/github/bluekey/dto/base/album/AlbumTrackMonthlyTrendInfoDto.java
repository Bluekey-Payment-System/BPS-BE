package com.github.bluekey.dto.base.album;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumTrackMonthlyTrendInfoDto {
    @Schema(description = "월", example = "7")
    private final int month;

    @Schema(description = "합", example = "2142344")
    private final long settlement;

    @Schema(description = "매출액", example = "732143")
    private final long revenue;
}
