package com.github.bluekey.dto.track;

import com.github.bluekey.dto.common.MemberBaseDto;
import com.github.bluekey.dto.album.AlbumBaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙 정산 금액")
public class TrackSettlementAmountDto {
	@Schema(description = "트랙 기본 정보")
	private TrackBaseDto track;
	@Schema(description = "앨범 기본 정보")
	private AlbumBaseDto album;
	@Schema(description = "트랙 참여 아티스트들 기본 정보")
	private List<MemberBaseDto> artists;
	@Schema(description = "트랙 매출액", example = "1000000")
	private Long revenue;
	@Schema(description = "회사 순이익", example = "100000")
	private Long netIncome;
	@Schema(description = "트랙 정산 금액", example = "900000")
	private Long settlementAmount;
	@Schema(description = "요율", example = "90")
	private Double commissionRate;

	@Builder
	public TrackSettlementAmountDto(final TrackBaseDto track, final AlbumBaseDto album, final List<MemberBaseDto> artists, final Long revenue, final Long netIncome, final Long settlementAmount, final Double commissionRate) {
		this.track = track;
		this.album = album;
		this.artists = artists;
		this.revenue = revenue;
		this.netIncome = netIncome;
		this.settlementAmount = settlementAmount;
		this.commissionRate = commissionRate;
	}
}
