package com.github.bluekey.validator.annotations;

import com.github.bluekey.validator.PasswordValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {PasswordValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidation {
	String message() default "8~16자의 영문 대/소문자, 숫자, 특수문자만 가능합니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
