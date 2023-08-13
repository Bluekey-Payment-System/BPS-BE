package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminArtistProfileListDto {
    @Schema(description = "아티스트 정보")
    private final List<ArtistProfileListDto> artist;

    @Schema(description = "매출액", example = "300")
    private final int revenue;
    @Schema(description = "순수익", example = "1234")
    private final int netIncome;
    @Schema(description = "정산액", example = "1234124")
    private final int settlementAmount;
    @Schema(description = "트랙명", example = "love")
    private final String representativeTrack;
    @Schema(description = "요율", example = "2.5")
    private final int monthlyIncreaseRate;
}
