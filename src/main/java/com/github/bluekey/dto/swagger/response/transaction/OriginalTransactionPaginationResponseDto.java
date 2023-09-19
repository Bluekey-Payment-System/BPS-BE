package com.github.bluekey.dto.swagger.response.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "OriginalTransactionPaginationResponseDto schema")
public class OriginalTransactionPaginationResponseDto {
    @Schema(description = "totalItems", example = "10")
    private int totalItems;

    @Schema(description = "contents")
    private List<OriginalTransactionResponseDto> contents = new ArrayList<>();

    public OriginalTransactionPaginationResponseDto(int totalItems, List<OriginalTransactionResponseDto> contents) {
        this.totalItems = totalItems;
        this.contents = contents;
    }
}
