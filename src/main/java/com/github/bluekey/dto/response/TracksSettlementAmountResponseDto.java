package com.github.bluekey.dto.response;

import com.github.bluekey.dto.TrackSettlementAmountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@Schema(description = "트랙별 정산 금액 리스트 (페이지네이션)")
public class TracksSettlementAmountResponseDto {
	private final Long totalItems;
	private final List<TrackSettlementAmountDto> contents;
}
