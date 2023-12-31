package com.github.bluekey.dto.swagger.admin;

import com.github.bluekey.dto.swagger.artist.ArtistProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "어드민 아티스트 프로필 리스트")
public class AdminArtistProfileListDto {
    @Schema(description = "아티스트 정보")
    private ArtistProfileDto artist;

    @Schema(description = "매출액", example = "300")
    private Integer revenue;

    @Schema(description = "순수익", example = "1234")
    private Integer netIncome;

    @Schema(description = "정산액", example = "1234124")
    private Integer settlementAmount;

    @Schema(description = "대표 트랙명", example = "love")
    private String representativeTrack;

    @Schema(description = "전월 대비 등락", example = "2.5")
    private Double monthlyIncreaseRate;

    @Builder
    public AdminArtistProfileListDto(final ArtistProfileDto artist, final Integer revenue,
            final Integer netIncome, final Integer settlementAmount, final String representativeTrack,
            final Double monthlyIncreaseRate) {
        this.artist = artist;
        this.revenue = revenue;
        this.netIncome = netIncome;
        this.settlementAmount = settlementAmount;
        this.representativeTrack = representativeTrack;
        this.monthlyIncreaseRate = monthlyIncreaseRate;
    }
}
