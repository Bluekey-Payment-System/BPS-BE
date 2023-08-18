package com.github.bluekey.dto.response;

import com.github.bluekey.dto.AlbumMonthlyAccontsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlbumMonthlyAccontsReponseDto {

    @Schema(description = "앨범의 월별 정산액 LIST")
    private final List<AlbumMonthlyAccontsDto> contents;
}
