package com.github.bluekey.dto.swagger.response.transaction;

import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.processor.ExcelRowException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "OriginalTransactionResponseDto schema")
public class OriginalTransactionResponseDto {

    @Schema(description = "id", example = "1")
    private Long id;
    @Schema(description = "name", example = "1202306_마피아 유통사 정산 내역.xlsx")
    private String name;

    @Schema(description = "uploadAt", example = "2023-08-23 17:24:54.642853", format="yyyy-MM-dd ~~")
    private LocalDateTime uploadAt;

    private List<ExcelRowException> warnings = new ArrayList<>();

    @Builder
    private OriginalTransactionResponseDto(final Long id, final String name, final LocalDateTime uploadAt, final List<ExcelRowException> warnings) {
        this.id = id;
        this.name = name;
        this.uploadAt = uploadAt;
        this.warnings = warnings;
    }

    public static OriginalTransactionResponseDto from(OriginalTransaction originalTransaction) {
        return OriginalTransactionResponseDto.builder()
                .id(originalTransaction.getId())
                .name(originalTransaction.getFileName())
                .uploadAt(originalTransaction.getCreatedAt())
                .warnings(new ArrayList<>())
                .build();
    }

    public static OriginalTransactionResponseDto fromWithWarning(OriginalTransaction originalTransaction, List<ExcelRowException> warnings) {
        return OriginalTransactionResponseDto.builder()
                .id(originalTransaction.getId())
                .name(originalTransaction.getFileName())
                .uploadAt(originalTransaction.getCreatedAt())
                .warnings(warnings)
                .build();
    }
}
