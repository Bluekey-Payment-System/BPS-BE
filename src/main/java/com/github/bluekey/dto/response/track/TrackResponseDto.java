package com.github.bluekey.dto.response.track;

import com.github.bluekey.dto.track.TrackCommissionRateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrackResponseDto {
    @Schema(description = "앨범ID", example = "1")
    private final Long albumId;

    @Schema(description = "앨범한글명", example = "사랑")
    private final String name;

    @Schema(description = "앨범영어명", example = "love")
    private final String enName;

    @Schema(description = "???", example = "true")
    private final boolean isOriginalTrack;

    @Schema(description = "트랙별의 요율 리스트")
    private final List<TrackCommissionRateDto> participants;
}
