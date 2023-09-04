package com.github.bluekey.exception.transaction;

import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.processor.ExcelRowException;
import lombok.Getter;

import java.util.List;

@Getter
public class ExcelUploadException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String fileName;
    private final List<ExcelRowException> errors;

    public ExcelUploadException(String fileName, List<ExcelRowException> errors) {
        this.errorCode = ErrorCode.TRANSACTION_INVALID_EXCEL_READER_VALUE;
        this.fileName = fileName;
        this.errors = errors;
    }
}
