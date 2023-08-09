package com.github.bluekey.controller;

import com.github.bluekey.dto.SwaggerTestDto;
import com.github.bluekey.dto.TempError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/swagger")
public class SwaggerController {

	/**
	 * swagger 적용을 위한 임시 controller 입니다.
	 */
	@Operation(summary = "swagger test", description = "swagger 적용을 위한 임시 controller 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "SwaggerTestDto 정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerTestDto.class))),
			@ApiResponse(responseCode = "404", description = "404 Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TempError.class))),
	})
	@GetMapping("/test")
	public SwaggerTestDto test() {
		return SwaggerTestDto.builder()
			.msg("test입니다!!^.^")
				.id(1L)
				.now(LocalDateTime.now())
			.build();
	}
}
