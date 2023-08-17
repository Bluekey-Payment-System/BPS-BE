package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrackInfoListDto {
    @Schema(description = "한글명", example = "봄이온다")
    private final String koTrackName;

    @Schema(description = "영어명", example = "Spring comes")
    private final String enTrackName;

    @Schema(description = "???", example = "true")
    private final boolean bluekeyOriginalTrack;

    @Schema(description = "트랙별의 요율 리스트")
    private final List<TrackParticipantsDto> participants;
}
