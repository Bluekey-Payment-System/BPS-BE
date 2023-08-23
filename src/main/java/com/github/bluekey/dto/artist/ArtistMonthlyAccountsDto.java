package com.github.bluekey.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Schema(description = "아티스트의 월별 정산 정보")
public class ArtistMonthlyAccountsDto {
    @Schema(description = "월", example = "7")
    private Integer month;

    @Schema(description = "정산액", example = "2142344")
    private Long settlement;

    @Schema(description = "매출", example = "732143")
    private Long revenue;

    @Builder
    public ArtistMonthlyAccountsDto(final Integer month, final Long settlement, final Long revenue) {
        this.month = month;
        this.settlement = settlement;
        this.revenue = revenue;
    }
}
