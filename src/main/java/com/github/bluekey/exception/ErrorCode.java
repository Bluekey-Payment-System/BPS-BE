package com.github.bluekey.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S_001", "서버에 오류가 발생했습니다."),


    // Authentication & Authorization Error
    AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "AU_001", "인증에 실패하였습니다."),
    AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "AR_002", "권한이 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AU_002", "아이디 또는 비밀번호가 일치하지 않습니다."),


    // JWT Error
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AU_003", "JWT가 존재하지 않아 유저 인증에 실패하였습니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "AU_004", "지원하지 않는 토큰입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "AU_005", "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "AU_006", "토큰 생성키가 올바르지 않습니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "AU_007","만료된 토큰입니다."),


    // General 400 Error
    INVALID_INPUT_TYPE(HttpStatus.BAD_REQUEST, "C_001", "입력값의 타입이 유효하지 않습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C_002", "입력값이 유효하지 않습니다."),
    INVALID_PWD_VALUE(HttpStatus.BAD_REQUEST, "AD_001", "8자 이상의 16자 이하의 숫자, 영문자, 특수문자를 포함한 비밀번호를 입력해주세요."),
    INVALID_EMAIL_VALUE(HttpStatus.BAD_REQUEST, "AD_001", "이메일 형식이 유효하지 않습니다."),


    // Not Found Error
    TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "T_001", "존재하지 않는 트랙입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M_001", "존재하지 않는 회원입니다."),
    ALBUM_NOT_FOUND(HttpStatus.NOT_FOUND, "A_001", "존재하지 않는 앨범입니다."),


    // Member 400 Error
    DUPLICATE_ARTIST_NAME(HttpStatus.CONFLICT, "AD_001", "이미 등록된 아티스트명과 동일한 닉네임은 사용할 수 없습니다."),
    INVALID_LOGIN_ID_VALUE(HttpStatus.CONFLICT, "AD_001", "이미 존재하는 아이디입니다."),
    MEMBER_ALREADY_REMOVED(HttpStatus.CONFLICT, "M_002", "이미 삭제된 회원입니다."),


    // File 400 Error
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "F_001", "파일 크기가 10MB를 초과하였습니다."),


    // General Pagination Error
    PAGINATION_INVALID_INDEX(HttpStatus.BAD_REQUEST, "PG_001", "페이지 인덱스가 올바르지 않습니다."),


    // Excel Validation Error
    EXCEL_INVALID_SHEET_NAME(HttpStatus.BAD_REQUEST, "E_001", "엑셀파일의 sheet명이 올바르지 않습니다."),
    EXCEL_INVALID_COLUMN_DEFINITION(HttpStatus.BAD_REQUEST, "E_002", "엑셀파일의 columns 정의가 올바르지 않습니다."),
    EXCEL_NOT_CONVERT_TO_WORKBOOK(HttpStatus.BAD_REQUEST, "E_003", "엑셀파일을 읽는 과정중에 이슈가 발생하였습니다."),


    // Transaction Error
    TRANSACTION_ALREADY_BATCH(HttpStatus.BAD_REQUEST, "TR_001", "{%s} 파일에 대해 이미 배치 작업이 완료된 transaction이 존재합니다."),
    ORIGINAL_TRANSACTION_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "TR_002", "이미 존재하는 엑셀파일이 존재합니다."),
    TRANSACTION_INVALID_EXCEL_READER_VALUE(HttpStatus.BAD_REQUEST, "TR_003", "엑셀 파일을 읽는 중 에러가 발생했습니다."),
    ORIGINAL_TRANSACTION_NOT_READ_FROM_S3(HttpStatus.BAD_REQUEST, "TR_004", "S3에서 해당 엑셀파일을 불러오는데 실패하였습니다."),
    ORIGINAL_TRANSACTION_INVALID_EXCEL_FILE_TYPE(HttpStatus.BAD_REQUEST, "TR_005", "엑셀파일 타입이 일치하지 않습니다."),
    ORIGINAL_TRANSACTION_NOT_EXIST(HttpStatus.NOT_FOUND, "TR_006", "엑셀파일 업로드 내역에서 해당하는 id를 가진 Original Transaction이 존재하지 않습니다."),
    TRANSACTION_INVALID_EXCEL_FILE_NAME(HttpStatus.BAD_REQUEST, "TR_007", "엑셀 파일명 포맷이 잘못됬습니다."),

    // Request Authority Error
    REQUEST_AUTHORITY_NOT_PENDING(HttpStatus.BAD_REQUEST, "RA_001", "해당 권한 요청은 PENDING 상태가 아닙니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
