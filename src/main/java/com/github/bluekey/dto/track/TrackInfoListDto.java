package com.github.bluekey.dto.track;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙 정보 리스트")
public class TrackInfoListDto {
    @Schema(description = "트랙ID", example = "1")
    private Long trackId;

    @Schema(description = "한글명", example = "봄이온다")
    private String name;

    @Schema(description = "영어명", example = "Spring comes")
    private String enName;

    @Schema(description = "???", example = "true")
    private boolean isOriginalTrack;

    @Schema(description = "트랙 참여 아티스트 리스트")
    private List<TrackArtistsDto> artists;

    @Builder
    public TrackInfoListDto(final Long trackId, final String name, final String enName, final boolean isOriginalTrack, final List<TrackArtistsDto> artists) {
        this.name = name;
        this.enName = enName;
        this.isOriginalTrack = isOriginalTrack;
        this.artists = artists;
    }
}
