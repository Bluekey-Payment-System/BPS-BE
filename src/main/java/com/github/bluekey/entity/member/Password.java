package com.github.bluekey.entity.member;

import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.GlobalExceptionHandler;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
	private static final Pattern PASSWORD_REGEX = Pattern.compile(
			"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");


	@Column(name = "password", nullable = false)
	private String value;

	public Password(final String value) {
		validatePassword(value);
		this.value = value;
	}

	private void validatePassword(final String password) {
		Assert.hasText(password, "password must not be null or empty.");
		// TODO: Exception class 새로 만들기
		if (password.length() < 8 || password.length() > 16) {
			throw new BusinessException(ErrorCode.INVALID_PWD_VALUE);
		}
		if (!PASSWORD_REGEX.matcher(password).matches()) {
			throw new BusinessException(ErrorCode.INVALID_PWD_VALUE);
		}
	}
}
