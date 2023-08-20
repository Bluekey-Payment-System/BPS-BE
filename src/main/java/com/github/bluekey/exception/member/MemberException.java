package com.github.bluekey.exception.member;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;

public class MemberException extends BusinessException {
	public MemberException() {
		super(ErrorCode.MEMBER_NOT_FOUND);
	}
}
