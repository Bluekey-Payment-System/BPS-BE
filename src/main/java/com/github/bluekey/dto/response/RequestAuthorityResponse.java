package com.github.bluekey.dto.response;

import com.github.bluekey.dto.common.MemberBase;
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
	private MemberBase sender;
	private LocalDateTime createdAt;

	@Builder
	public RequestAuthorityResponse(final Long requestAuthorityId,
			final MemberBase sender, final LocalDateTime createdAt) {
		this.requestAuthorityId = requestAuthorityId;
		this.sender = sender;
		this.createdAt = createdAt;
	}
}
