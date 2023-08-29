package com.github.bluekey.exception.transaction;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;

public class AlreadyOriginalTransactionExistException extends BusinessException {

    public AlreadyOriginalTransactionExistException() {
        super(ErrorCode.ORIGINAL_TRANSACTION_ALREADY_EXIST);
    }
}

