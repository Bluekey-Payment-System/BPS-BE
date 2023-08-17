package com.github.bluekey.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.bluekey.processor.ExcelRowException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "엑셀 파일 업로드 시에 반환하는 ErrorResponse 타입")
public class UploadErrorResponse {

    @JsonIgnore
    private HttpStatus status;
    private String code;
    private String message;
    @Schema(description = "엑셀 파일 기준으로 총 에러가 발생한 행의 갯수", example = "10")
    private int totalErrorNums;
    private List<ExcelRowException> errors = new ArrayList<>();

    @Builder
    private UploadErrorResponse(final HttpStatus status, final String code, final String message, List<ExcelRowException> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.totalErrorNums = errors.size();
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
