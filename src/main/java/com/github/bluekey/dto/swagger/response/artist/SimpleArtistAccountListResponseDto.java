package com.github.bluekey.dto.swagger.response.artist;

import com.github.bluekey.dto.swagger.artist.SimpleArtistAccountDto;
import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙 추가 시 드롭다운 UI에 필요한 아티스트 리스트 응답 DTO")
public class SimpleArtistAccountListResponseDto {

    @Schema(description = "아티스트 리스트 정보")
    private List<SimpleArtistAccountDto> artists;

    @Builder
    private SimpleArtistAccountListResponseDto(List<SimpleArtistAccountDto> artists) {
        this.artists = artists;
    }

    public static SimpleArtistAccountListResponseDto from(List<Member> artists) {
        return SimpleArtistAccountListResponseDto.builder()
                .artists(artists.stream().map(SimpleArtistAccountDto::new).collect(Collectors.toList()))
                .build();
    }
}
