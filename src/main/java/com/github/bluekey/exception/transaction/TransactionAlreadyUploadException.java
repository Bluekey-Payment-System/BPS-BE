package com.github.bluekey.exception.transaction;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;

public class TransactionAlreadyUploadException extends BusinessException {

    public TransactionAlreadyUploadException(String alreadyBatchFileName) {
        super(ErrorCode.TRANSACTION_ALREADY_BATCH, alreadyBatchFileName);
    }
}
