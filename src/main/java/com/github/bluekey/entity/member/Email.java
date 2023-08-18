package com.github.bluekey.entity.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$");

    @Column(name = "email", nullable = false)
    private String value;

    public Email(final String value) {
        validateEmail(value);
        this.value = value;
    }

    private void validateEmail(final String email) {
        Assert.hasText(email, "Email must not be null or empty.");
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
