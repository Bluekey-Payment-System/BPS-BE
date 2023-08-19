package com.github.bluekey.dto.request;

import com.github.bluekey.dto.base.track.TrackCommissionRateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrackRequestDto {
    @Schema(description = "한글 트랙명", example = "아이엠")
    private final String name;

    @Schema(description = "영문 트랙명", example = "I AM")
    private final String enName;

    @Schema(description = "블루키 오리지널 트랙", example = "false")
    private final boolean isOriginalTrack;

    @Schema(description = "트랙별 요율정보")
    private final List<TrackCommissionRateDto> participants;

}
