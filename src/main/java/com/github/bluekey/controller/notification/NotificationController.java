package com.github.bluekey.controller.notification;

import com.github.bluekey.config.security.jwt.PrincipalConvertUtil;
import com.github.bluekey.dto.common.ListResponse;
import com.github.bluekey.dto.response.RequestAuthorityPendingStatusResponseDto;
import com.github.bluekey.dto.response.RequestAuthorityResponse;
import com.github.bluekey.dto.response.RequestAuthorityUpdateResponseDto;
import com.github.bluekey.dto.swagger.response.artist.SimpleArtistAccountListResponseDto;
import com.github.bluekey.service.notification.RequestAuthorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

	private final RequestAuthorityService requestAuthorityService;

	@Operation(summary = "권한 요청 목록", description = "권한 요청 목록 가져오기")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환"),
	})
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/request-authorities")
	public ListResponse<RequestAuthorityResponse> getRequestAuthority() {
		return requestAuthorityService.getRequestAuthority(PrincipalConvertUtil.getMemberId());
	}

	@Operation(summary = "권한 요청", description = "로그인한 유저가 슈퍼 어드민에게 권한을 요청")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "No Content"),
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasRole('PENDING') or hasRole('ADMIN')")
	@PostMapping("/request-authorities")
	public void requestAuthority() {
		requestAuthorityService.requestAuthority(PrincipalConvertUtil.getMemberId());
	}

	@Operation(summary = "권한 요청 승인", description = "권한 요청 승인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환"),
	})
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PatchMapping("/request-authorities/{requestAuthorityId}/approve")
	public ResponseEntity<RequestAuthorityUpdateResponseDto> approveRequestAuthority(
			@PathVariable("requestAuthorityId") Long requestAuthorityId
	) {
		RequestAuthorityUpdateResponseDto response = requestAuthorityService.approveAuthority(PrincipalConvertUtil.getMemberId(), requestAuthorityId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "권한 요청 거절", description = "권한 요청 거절")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환"),
	})
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PatchMapping("/request-authorities/{requestAuthorityId}/reject")
	public ResponseEntity<RequestAuthorityUpdateResponseDto> rejectRequestAuthority(
			@PathVariable("requestAuthorityId") Long requestAuthorityId
	) {
		RequestAuthorityUpdateResponseDto response = requestAuthorityService.rejectAuthority(PrincipalConvertUtil.getMemberId(), requestAuthorityId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Pending 상태 권한 요청 여부", description = "알림 여부를 제공하기 위한 Pending status인 권한 요청이 있는지에 대한 여부 데이터 반환")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환",
					content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = RequestAuthorityPendingStatusResponseDto.class)))
	})
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/status/pending")
	public ResponseEntity<RequestAuthorityPendingStatusResponseDto> hasRequestAuthority() {
		return ResponseEntity.ok(requestAuthorityService.hasPendingRequestAuthority(PrincipalConvertUtil.getMemberId()));
	}
}
