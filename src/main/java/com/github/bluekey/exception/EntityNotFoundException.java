package com.github.bluekey.exception;

public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
