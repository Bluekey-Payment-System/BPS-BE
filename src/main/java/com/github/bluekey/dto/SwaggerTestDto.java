package com.github.bluekey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "swagger 적용을 위한 예시 dto")
public class SwaggerTestDto {

	/**
	 * swagger 적용을 위한 예시 dto
	 * 삭제할 예정입니다.
	 */
	@Schema(description = "id", example = "1")
	private final Long id;
	@Schema(description = "msg", example = "test입니다!!^.^")
	private final String msg;
	@Schema(description = "time", example = "2023-08-31T15:00:00")
	private final LocalDateTime now;
}
