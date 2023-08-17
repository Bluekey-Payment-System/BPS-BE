package com.github.bluekey.dto.response;

import com.github.bluekey.dto.AdminArtistProfileListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminArtistProfileListReponseDto {
    @Schema(description = "???", example = "300")
    private final int totalItems;

    @Schema(description = "아티스트 상세 정보")
    private final List<AdminArtistProfileListDto> contents;
}
