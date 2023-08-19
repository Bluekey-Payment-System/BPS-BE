package com.github.bluekey.dto.response;

import com.github.bluekey.dto.base.artist.ArtistMonthlyAccountsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArtistMonthlyAccountsReponseDto {
    @Schema(description = "아티스트 List 정보")
    private final List<ArtistMonthlyAccountsDto> contents;
}
