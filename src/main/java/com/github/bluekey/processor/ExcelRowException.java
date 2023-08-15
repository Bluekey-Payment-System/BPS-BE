package com.github.bluekey.processor;

import com.github.bluekey.processor.type.ExcelRowExceptionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelRowException {
    private int rowIndex;
    private int columnIndex;
    private String columnName;
    private ExcelRowExceptionType type;
    private String severity;
    private String message;

    @Builder
    public ExcelRowException(final int rowIndex, final int columnIndex, final String columnName, final ExcelRowExceptionType type, final String columnValue, final String domain) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.type = type;
        this.severity = type.getType().name();
        this.message = String.format(type.getMessage(), columnValue, domain);
    }
}