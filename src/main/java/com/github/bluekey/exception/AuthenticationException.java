package com.github.bluekey.exception;

public class AuthenticationException extends BusinessException{

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
