package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "트랙 정산 금액")
public class TrackSettlementAmountDto {
	@Schema(description = "트랙 기본 정보")
	private final CommonDto track;
	@Schema(description = "앨범 기본 정보")
	private final CommonDto album;
	@Schema(description = "트랙 참여 아티스트들 기본 정보")
	private final List<CommonDto> artists;
	@Schema(description = "트랙 매출액", example = "1000000")
	private final Long revenue;
	@Schema(description = "회사 순이익", example = "100000")
	private final Long netIncome;
	@Schema(description = "트랙 정산 금액", example = "900000")
	private final Long settlementAmount;
	@Schema(description = "요율", example = "90")
	private final Double CommissionRate;
}
