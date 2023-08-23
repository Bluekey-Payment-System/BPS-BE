package com.github.bluekey.dto.album;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.criteria.CriteriaBuilder.In;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 월별 정산")
public class AlbumMonthlyAccontsDto {
    @Schema(description = "월", example = "7")
    private Integer month;

    @Schema(description = "정산액", example = "2142344")
    private Long settlement;

    @Schema(description = "매출", example = "732143")
    private Long revenue;

    @Builder
    public AlbumMonthlyAccontsDto(final Integer month, final Long settlement, final Long revenue) {
        this.month = month;
        this.settlement = settlement;
        this.revenue = revenue;
    }
}
