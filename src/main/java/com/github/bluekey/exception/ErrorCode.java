package com.github.bluekey.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Internal Server error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S_001", "서버에 오류가 발생했습니다."),

    AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "AR_001", "권한이 없습니다."),
    AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "AU_001", "인증에 실패하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
