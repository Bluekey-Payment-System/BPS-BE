package com.github.bluekey.exception.member;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;

public class MemberNotFoundException extends BusinessException {
	public MemberNotFoundException() {
		super(ErrorCode.MEMBER_NOT_FOUND);
	}
}
