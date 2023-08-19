package com.github.bluekey.dto.response;

import com.github.bluekey.dto.base.track.TrackSettlementAmountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Deprecated
@Schema(description = "트랙별 정산 금액 리스트 (페이지네이션)")
public class TracksSettlementAmountResponseDto {
	@Schema(description = "총 아이템 개수")
	private final Long totalItems;
	@Schema(description = "해당 페이지의 트랙별 정산 금액 리스트")
	private final List<TrackSettlementAmountDto> contents;
}
