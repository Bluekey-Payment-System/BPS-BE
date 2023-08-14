package com.github.bluekey.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Internal Server error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S_001", "서버에 오류가 발생했습니다."),

    AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "AR_001", "권한이 없습니다."),
    AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "AU_001", "인증에 실패하였습니다."),

    // 400~
    INVALID_INPUT_TYPE(HttpStatus.BAD_REQUEST, "C_001", "입력값의 타입이 유효하지 않습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C_002", "입력값이 유효하지 않습니다."),

    INVALID_PWD_VALUE(HttpStatus.BAD_REQUEST, "AD_001", "8자 이상의 16자 이하의 숫자, 영문자, 특수문자를 포함한 비밀번호를 입력해주세요."),
    INVALID_EMAIL_VALUE(HttpStatus.BAD_REQUEST, "AD_001", "이메일 형식이 유효하지 않습니다."),
    INVALID_NICKNAME_VALUE(HttpStatus.BAD_REQUEST, "AD_001", "이미 존재하는 닉네임입니다."),

    // Transaction 400~
    TRANSACTION_ALREADY_BATCH(HttpStatus.BAD_REQUEST, "T_001", "{%s} 파일에 대해 이미 배치 작업이 완료된 transaction이 존재합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
