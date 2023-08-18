package com.github.bluekey.exception.transaction;

import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.processor.ExcelRowException;
import lombok.Getter;

import java.util.List;

@Getter
public class ExcelUploadException extends RuntimeException{
    private final ErrorCode errorCode;
    private final List<ExcelRowException> errors;

    public ExcelUploadException(List<ExcelRowException> errors) {
        this.errorCode = ErrorCode.TRANSACTION_INVALID_EXCEL_READER_VALUE;
        this.errors = errors;
    }
}
