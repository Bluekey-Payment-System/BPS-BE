package com.github.bluekey.dto.response;

import com.github.bluekey.dto.common.AdminBase;
import com.github.bluekey.entity.notification.RequestStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestAuthorityResponse {
	private Long requestAuthorityId;
	private AdminBase sender;
	private RequestStatus status;
	private LocalDateTime createdAt;

	@Builder
	public RequestAuthorityResponse(final Long requestAuthorityId, final AdminBase sender,
			final RequestStatus status, final LocalDateTime createdAt) {
		this.requestAuthorityId = requestAuthorityId;
		this.sender = sender;
		this.status = status;
		this.createdAt = createdAt;
	}
}
