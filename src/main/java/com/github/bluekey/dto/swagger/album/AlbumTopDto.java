package com.github.bluekey.dto.swagger.album;

import com.github.bluekey.dto.swagger.common.TrackBaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 당월 매출 TOP n 트랙")
public class AlbumTopDto {
    @Schema(description = "트랙 정보")
    private TrackBaseDto track;

    @Schema(description = "매출액", example = "1000")
    private double revenue;

    @Schema(description = "매출액 증감률", example = "9.9")
    private Double growthRate;

    @Schema(description = "매출액 비율", example = "12")
    private double proportion;

    @Builder
    public AlbumTopDto(final TrackBaseDto track, final double revenue, final Double growthRate, final double proportion) {
        this.track = track;
        this.revenue = revenue;
        this.growthRate = growthRate;
        this.proportion = proportion;
    }

}
