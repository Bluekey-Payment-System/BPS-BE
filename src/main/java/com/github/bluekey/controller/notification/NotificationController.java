package com.github.bluekey.controller.notification;

import com.github.bluekey.config.security.jwt.PrincipalConvertUtil;
import com.github.bluekey.service.notification.RequestAuthorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

	private final RequestAuthorityService requestAuthorityService;

	@Operation(summary = "권한 요청 승인", description = "권한 요청 승인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환"),
	})
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PatchMapping("/request-authority/{requestAuthorityId}/approve")
	public void approveRequestAuthority(
			@PathVariable("requestAuthorityId") Long requestAuthorityId
	) {
		requestAuthorityService.approveAuthority(PrincipalConvertUtil.getMemberId(), requestAuthorityId);
	}
}
