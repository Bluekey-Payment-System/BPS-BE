package com.github.bluekey.controller.member;


import com.github.bluekey.dto.request.auth.PasswordRequestDto;
import com.github.bluekey.dto.request.auth.LoginRequestDto;
import com.github.bluekey.dto.request.auth.SignupRequestDto;
import com.github.bluekey.dto.response.auth.LoginTokenResponseDto;
import com.github.bluekey.dto.response.auth.MemberIdResponseDto;
import com.github.bluekey.dto.response.auth.SignupResponseDto;
import com.github.bluekey.jwt.PrincipalConvertUtil;
import com.github.bluekey.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth 관련 API")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "admin 로그인", description = "admin 로그인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginTokenResponseDto.class))),
	})
	@PostMapping("/admin/login")
	public ResponseEntity<LoginTokenResponseDto> adminLogin(@RequestBody LoginRequestDto dto) {
		return ResponseEntity.ok(authService.login(dto));
	}

	@Operation(summary = "admin 회원가입", description = "admin 회원가입")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupResponseDto.class))),
	})
	@PostMapping("/admin/signup")
	public ResponseEntity<SignupResponseDto> adminSignup(@Valid @RequestBody SignupRequestDto dto) {
		return ResponseEntity.ok(authService.createAdmin(dto));
	}

	@Operation(summary = "member 로그인", description = "member 로그인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginTokenResponseDto.class))),
	})
	@PostMapping("/member/login")
	public ResponseEntity<LoginTokenResponseDto> memberLogin(@RequestBody LoginRequestDto dto) {
		return ResponseEntity.ok(authService.login(dto));
	}

	@Operation(summary = "member 비밀번호 변경", description = "member 비밀번호 변경")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
	})
	@PatchMapping("/member/password")
	public ResponseEntity<?> passwordChange(@Valid @RequestBody PasswordRequestDto dto) {
		authService.changePassword(dto, PrincipalConvertUtil.getMemberId());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "member 비밀번호 확인", description = "member 비밀번호 확인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "비밀번호 확인 성공"),
	})
	@PostMapping("/member/password/confirm")
	public ResponseEntity<?> passwordCheck(@RequestBody PasswordRequestDto dto) {
		authService.matchPassword(dto, PrincipalConvertUtil.getMemberId());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "member 퇴출", description = "Super Admin이 member에 대해 탈퇴를 진행")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "퇴출 성공"),
	})
	@DeleteMapping("/members/{memberId}/withdrawal")
	public ResponseEntity<MemberIdResponseDto> withdrawal(@PathVariable("memberId") Long memberId) {
		Long removedMemberId = authService.deleteMember(memberId);
		return ResponseEntity.ok(MemberIdResponseDto.builder().memberId(removedMemberId).build());
	}
}
