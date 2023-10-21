package com.github.bluekey.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Pending 권한 요청 여부")
public class RequestAuthorityPendingStatusResponseDto {

    @Schema(description = "PENDING status인 알림 존재 여부", example = "true")
    private boolean hasPendingRequestAuthority;

    public RequestAuthorityPendingStatusResponseDto(boolean hasPendingRequestAuthority) {
        this.hasPendingRequestAuthority = hasPendingRequestAuthority;
    }
}
