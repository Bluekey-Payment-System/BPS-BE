package com.github.bluekey.dto.request.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "파일 업로드 요청")
public class OriginalTransactionRequestDto {

    @Schema(description = "유통사 엑셀 파일을 업로드 하는 시점", example = "2023-08")
    private String uploadAt;

    public OriginalTransactionRequestDto(final String uploadAt) {
        this.uploadAt = uploadAt;
    }
}
