package com.github.bluekey.dto.track;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙 참여 아티스트 리스트")
public class TrackArtistsDto {
    @Schema(description = "아티스트 고유 id", example = "1")
    private Long memberId;

    @Schema(description = "한글명", example = "김레드")
    private String name;

    @Schema(description = "영어명", example = "kimRed")
    private String enName;

    @Schema(description = "요율", example = "15")
    private Double commissionRate;

    @Builder
    public TrackArtistsDto(final Long memberId, final String name, final String enName, final Double commissionRate) {
        this.memberId = memberId;
        this.name = name;
        this.enName = enName;
        this.commissionRate = commissionRate;
    }
}
