package com.github.bluekey.dto.request.transaction;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginalTransactionRequestDto {
    private String uploadAt;

    public OriginalTransactionRequestDto(final String uploadAt) {
        this.uploadAt = uploadAt;
    }
}
