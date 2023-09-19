package com.github.bluekey.dto.swagger.response.artist;

import com.github.bluekey.dto.swagger.artist.ArtistMonthlyAccountsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistMonthlyAccountsResponseDto {
    @Schema(description = "아티스트 List 정보")
    private final List<ArtistMonthlyAccountsDto> contents;
}
