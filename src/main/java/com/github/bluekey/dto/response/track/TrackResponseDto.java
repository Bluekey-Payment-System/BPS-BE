package com.github.bluekey.dto.response.track;

import com.github.bluekey.dto.track.TrackCommissionRateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙 정보")
public class TrackResponseDto {
    @Schema(description = "트랙ID", example = "1")
    private Long trackId;

    @Schema(description = "앨범ID", example = "1")
    private Long albumId;

    @Schema(description = "앨범한글명", example = "사랑")
    private String name;

    @Schema(description = "앨범영어명", example = "love")
    private String enName;

    @Schema(description = "블루키 오리지널 뮤직", example = "true")
    private boolean isOriginalTrack;

    @Schema(description = "참여한 아티스트 리스트")
    private List<TrackCommissionRateDto> artists;

    @Builder
    public TrackResponseDto(final Long trackId, final Long albumId, final String name,
            final String enName, final boolean isOriginalTrack, final List<TrackCommissionRateDto> artists) {
        this.trackId = trackId;
        this.albumId = albumId;
        this.name = name;
        this.enName = enName;
        this.isOriginalTrack = isOriginalTrack;
        this.artists = artists;
    }
}
