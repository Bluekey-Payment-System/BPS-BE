package com.github.bluekey.dto.response.admin;

import com.github.bluekey.dto.admin.AdminArtistProfileListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminArtistProfileListReponseDto {
    @Schema(description = "총 아이템 개수", example = "300")
    private final int totalItems;

    @Schema(description = "아티스트 상세 정보")
    private final List<AdminArtistProfileListDto> contents;
}
