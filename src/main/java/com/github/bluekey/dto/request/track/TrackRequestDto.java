package com.github.bluekey.dto.request.track;

import com.github.bluekey.dto.track.TrackCommissionRateDto;
import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Schema(description = "새로 등록할 트랙 정보")
public class TrackRequestDto {
    @Schema(description = "한글 트랙명", example = "아이엠")
    private String name;

    @Schema(description = "영문 트랙명", example = "I AM")
    private String enName;

    @Schema(description = "블루키 오리지널 트랙", example = "false")
    private Boolean isOriginalTrack;

    @Schema(description = "트랙별 요율정보")
    private List<TrackCommissionRateDto> artists;

    @Builder
    public TrackRequestDto(final String name, final String enName, final Boolean isOriginalTrack,
            final List<TrackCommissionRateDto> artists) {
        this.name = name;
        this.enName = enName;
        this.isOriginalTrack = isOriginalTrack;
        this.artists = artists;
    }

    public Track toTrack(Album album) {
        return Track.ByTrackBuilder()
                .album(album)
                .name(name)
                .enName(enName)
                .isOriginalTrack(isOriginalTrack)
                .build();
    }
}
