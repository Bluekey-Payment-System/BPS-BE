package com.github.bluekey.dto.response;

import com.github.bluekey.dto.ArtistInfoDto;
import com.github.bluekey.dto.TrackInfoListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlbumTrackListReponseDto {

    @Schema(description = "이미지", example = "https://s3...")
    private final String albumImage;

    @Schema(description = "한글앨범명", example = "아름다운 세상")
    private final String name;

    @Schema(description = "영어앨범명", example = "Beautiful")
    private final String enName;

    @Schema(description = "아티스트 정보")
    private final List<ArtistInfoDto> artist;

    @Schema(description = "트랙별의 리스트")
    private final List<TrackInfoListDto> tracks;
}
