package com.github.bluekey.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.bluekey.processor.ExcelRowException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadErrorResponse {

    @JsonIgnore
    private HttpStatus status;
    private String code;
    private String message;
    private List<ExcelRowException> errors = new ArrayList<>();

    @Builder
    private UploadErrorResponse(final HttpStatus status, final String code, final String message, List<ExcelRowException> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static UploadErrorResponse of(ErrorCode errorCode, List<ExcelRowException> errors) {
        return UploadErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .errors(errors)
                .build();
    }
}