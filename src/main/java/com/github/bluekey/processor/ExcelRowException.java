package com.github.bluekey.processor;

import com.github.bluekey.processor.type.ExcelRowExceptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "엑셀파일을 업로드 할 때 에러가 발생한 Row와 Cell에 대한 데이터")
public class ExcelRowException {
    @Schema(description = "에러가 발생한 행의 인덱스", example = "15")
    private int rowIndex;

    @Schema(description = "에러가 발생한 열의 인덱스", example = "4")
    private int columnIndex;
    private String columnName;
    private String columnValue;
    private ExcelRowExceptionType type;
    private String severity;
    private String message;

    @Builder
    public ExcelRowException(final int rowIndex, final int columnIndex, final String columnName, final ExcelRowExceptionType type, final String columnValue) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.columnValue = columnValue;
        this.type = type;
        this.severity = type.getType().name();
        this.message = String.format(type.getMessage(), columnValue);
    }
}