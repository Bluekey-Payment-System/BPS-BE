package com.github.bluekey.dto.swagger.response.admin;

import com.github.bluekey.dto.swagger.admin.AdminArtistProfileListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "어드민 아티스트 프로필 리스트")
public class AdminArtistProfileListReponseDto {
    @Schema(description = "총 아이템 개수", example = "300")
    private int totalItems;

    @Schema(description = "아티스트 상세 정보")
    private List<AdminArtistProfileListDto> contents;

    @Builder
    public AdminArtistProfileListReponseDto(final int totalItems, final List<AdminArtistProfileListDto> contents) {
        this.totalItems = totalItems;
        this.contents = contents;
    }
}
