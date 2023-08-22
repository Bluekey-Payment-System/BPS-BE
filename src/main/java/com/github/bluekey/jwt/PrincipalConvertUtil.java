package com.github.bluekey.jwt;

import com.github.bluekey.exception.AuthorizationException;
import com.github.bluekey.exception.ErrorCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class PrincipalConvertUtil {

	private PrincipalConvertUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static Long getMemberId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = (UserDetails) principal;
		if (userDetails.getUsername() == null) {
			throw new AuthorizationException(ErrorCode.AUTHORIZATION_FAILED);
		}
		return Long.parseLong(userDetails.getUsername());
	}
}
