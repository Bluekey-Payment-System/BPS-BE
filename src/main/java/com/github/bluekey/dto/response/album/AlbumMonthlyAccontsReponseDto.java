package com.github.bluekey.dto.response.album;

import com.github.bluekey.dto.album.AlbumMonthlyAccontsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "앨범의 월별 정산액 LIST")
public class AlbumMonthlyAccontsReponseDto {

    @Schema(description = "앨범의 월별 정산액 LIST")
    private List<AlbumMonthlyAccontsDto> contents;

    @Builder
    public AlbumMonthlyAccontsReponseDto(final List<AlbumMonthlyAccontsDto> contents) {
        this.contents = contents;
    }
}
