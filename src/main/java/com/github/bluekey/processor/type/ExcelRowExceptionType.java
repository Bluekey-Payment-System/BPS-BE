package com.github.bluekey.processor.type;

import lombok.Getter;

@Getter
public enum ExcelRowExceptionType {
    NULL_CELL(ExcelRowExceptionSeverityType.DANGER, "값이 비어 있는 셀입니다."),
    NOT_EXIST(ExcelRowExceptionSeverityType.DANGER, "현재 {%s} 이름의 셀의 값은 {%s} 테이블에 존재하지 않습니다."),
    INVALID_CELL_VALUE_TYPE(ExcelRowExceptionSeverityType.DANGER, "현재 셀의 값의 타입이 올바르지 않습니다"),
    ALLOW_EXCEPTION_CASE(ExcelRowExceptionSeverityType.WARNING, "예외로 허용되는 케이스입니다.");

    private final ExcelRowExceptionSeverityType type;
    private final String message;

    ExcelRowExceptionType(ExcelRowExceptionSeverityType type, String message) {
        this.type = type;
        this.message = message;
    }
}
