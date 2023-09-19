package com.github.bluekey.dto.swagger.response.album;

import com.github.bluekey.dto.swagger.artist.ArtistInfoDto;
import com.github.bluekey.dto.swagger.track.TrackInfoListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 트랙 리스트 응답")
public class AlbumTrackListResponseDto {
    @Schema(description = "앨범ID", example = "1")
    private Long albumId;

    @Schema(description = "이미지", example = "https://s3...")
    private String albumImage;

    @Schema(description = "한글앨범명", example = "아름다운 세상")
    private String name;

    @Schema(description = "영어앨범명", example = "Beautiful")
    private String enName;

    @Schema(description = "대표 아티스트 정보")
    private ArtistInfoDto artist;

    @Schema(description = "트랙별의 리스트")
    private List<TrackInfoListDto> tracks;

    @Builder
    public AlbumTrackListResponseDto(final Long albumId, final String albumImage, final String name,
            final String enName, final ArtistInfoDto artist, final List<TrackInfoListDto> tracks) {
        this.albumId = albumId;
        this.albumImage = albumImage;
        this.name = name;
        this.enName = enName;
        this.artist = artist;
        this.tracks = tracks;
    }
}
