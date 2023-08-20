package com.github.bluekey.controller.member;


import com.github.bluekey.dto.request.PasswordRequestDto;
import com.github.bluekey.dto.request.LoginRequestDto;
import com.github.bluekey.dto.request.SignupRequestDto;
import com.github.bluekey.dto.response.LoginTokenResponseDto;
import com.github.bluekey.dto.response.SignupResponseDto;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.ErrorResponse;
import com.github.bluekey.jwt.PrincipalConvertUtil;
import com.github.bluekey.service.auth.AuthService;
import com.github.bluekey.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
			@ApiResponse(responseCode = "500", description = "internal server error", content = {})
	})
	@PostMapping("/admin/login")
	public LoginTokenResponseDto adminLogin(@RequestBody LoginRequestDto dto) {
		return authService.login(dto);
	}

	//TODO: 400 bad request가 필드마다 다르게 나올 수 있음, 아직 처리 못 했으나 나중에 처리할 것
	@Operation(summary = "admin 회원가입", description = "admin 회원가입")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 필드값", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "internal server error", content = {})
	})
	@PostMapping("/admin/signup")
	public SignupResponseDto adminSignup(@Validated @RequestBody SignupRequestDto dto) {
		return authService.createAdmin(dto);
	}

	@Operation(summary = "member 로그인", description = "member 로그인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginTokenResponseDto.class))),
			@ApiResponse(responseCode = "500", description = "internal server error", content = {})
	})
	@PostMapping("/member/login")
	public LoginTokenResponseDto memberLogin(@RequestBody LoginRequestDto dto) {
		return authService.login(dto);
	}

	@Operation(summary = "member 비밀번호 변경", description = "member 비밀번호 변경")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 비밀번호", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "internal server error", content = {})
	})
	@PatchMapping("/member/password")
	public void passwordChange(@Validated @RequestBody PasswordRequestDto dto) {
		authService.changePassword(dto, PrincipalConvertUtil.getMemberId());
	}

	@Operation(summary = "member 비밀번호 확인", description = "member 비밀번호 확인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "비밀번호 확인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
			@ApiResponse(responseCode = "400", description = "비밀번호 일치하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "internal server error", content = {})
	})
	@PostMapping("/member/password/confirm")
	public void passwordCheck(@RequestBody PasswordRequestDto dto) {
		if (!authService.matchPassword(dto, PrincipalConvertUtil.getMemberId())) {
			throw new BusinessException(ErrorCode.NO_MATCH_PWD_VALUE);
		}
	}

	@Operation(summary = "member 퇴출", description = "Super Admin이 member에 대해 탈퇴를 진행")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "퇴출 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
			@ApiResponse(responseCode = "500", description = "internal server error", content = {})
	})
	@DeleteMapping("/members/{memberId}/withdrawal")
	public void withdrawal(@PathVariable("memberId") Long memberId) {

	}
}
