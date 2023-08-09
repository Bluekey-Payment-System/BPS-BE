package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "swagger 적용을 위한 예시 error dto")
public class TempError {
	/**
	 * swagger 적용을 위한 예시 error dto
	 * 삭제할 예정입니다.
	 */
	@Schema(description = "error code", example = "404 Not Found")
	private final String statusCode;
	@Schema(description = "error message", example = "멤버를 찾을 수 없습니다.")
	private final String message;
}
