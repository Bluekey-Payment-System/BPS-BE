package com.github.bluekey.dto.response.track;

import com.github.bluekey.dto.track.TrackSettlementAmountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙별 정산 금액 리스트 (페이지네이션)")
public class TracksSettlementAmountResponseDto {
	@Schema(description = "총 아이템 개수")
	private int totalItems;
	@Schema(description = "해당 페이지의 트랙별 정산 금액 리스트")
	private List<TrackSettlementAmountDto> contents;

	@Builder
	public TracksSettlementAmountResponseDto(final int totalItems, final List<TrackSettlementAmountDto> contents) {
		this.totalItems = totalItems;
		this.contents = contents;
	}
}
