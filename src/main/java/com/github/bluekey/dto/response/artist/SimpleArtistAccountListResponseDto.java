package com.github.bluekey.dto.response.artist;

import com.github.bluekey.dto.artist.SimpleArtistAccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙 추가 시 드롭다운 UI에 필요한 아티스트 리스트 응답 DTO")
public class SimpleArtistAccountListResponseDto {

    @Schema(description = "아티스트 리스트 정보")
    private List<SimpleArtistAccountDto> artists;

    @Builder
    public SimpleArtistAccountListResponseDto(List<SimpleArtistAccountDto> artists) {
        this.artists = artists;
    }
}
