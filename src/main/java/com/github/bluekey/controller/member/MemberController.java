package com.github.bluekey.controller.member;

import com.github.bluekey.dto.response.AdminAndArtistAccountResponseDto;
import com.github.bluekey.dto.response.ArtistAccountResponseDto;
import com.github.bluekey.dto.response.ArtistAlbumsListReponseDto;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
@RequestMapping("/api/v1/Members")
@RequiredArgsConstructor
public class MemberController {

	@Operation(summary = "관리자 계정 관리", description = "관리자가 관리하는 계정들을 불러온다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사이트 이용자 계정 정보 LIST 반환",
			content = @Content(mediaType = "application/json", schema = @Schema(
					oneOf = {ArtistAccountResponseDto.class, AdminAndArtistAccountResponseDto.class}))),
	})
	@GetMapping
	public ResponseEntity<?> getMemberList(
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size
	) {
		// TODO: MemberRole.ADMIN -> ArtistAccountResponseDto
		// TODO: MemberRole.SUPER_ADMIN -> AdminAndAristAccountResponseDto
		return null;
	}
}
