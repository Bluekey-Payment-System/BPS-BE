package com.github.bluekey.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Bluekey 프로젝트 API", version = "v1"))
@RequiredArgsConstructor
public class SwaggerConfig {
	//TODO: 인증에 대한 부분 추가
	@Bean
	public OpenAPI getOpenAPI() {
		return new OpenAPI().components(new Components());
	}
}
