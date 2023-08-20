package com.github.bluekey.validator;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.validator.annotations.PasswordValidation;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
	private static final Pattern PASSWORD_REGEX = Pattern.compile(
			"^[a-zA-Z\\d@$!%*?&-_]+$"
	);
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null) {
			return false;
		}
		if (password.length() < 8 || password.length() > 16) {
			return false;
		}
		if (!PASSWORD_REGEX.matcher(password).matches()) {
			return false;
		}
		return true;
	}
}
