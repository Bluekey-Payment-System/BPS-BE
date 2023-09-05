package com.github.bluekey.config.security;

import com.github.bluekey.jwt.JwtAuthenticationFilter;
import com.github.bluekey.jwt.JwtProvider;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

	@Value("${spring.server.be-host}")
	private String beHost;
	@Value("${spring.server.fe-host}")
	private String feHost;
	private final JwtProvider jwtProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic().disable()
				.csrf().disable()
				.cors(c -> {
					c.configurationSource(request -> {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(List.of("http://localhost:3000", beHost, feHost));
						config.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
						config.setAllowedHeaders(Arrays.asList("*"));
						return config;
					});
				})
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/api/v1/auth/admin/**", "/api/v1/auth/member/login", "/h2-console/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.exceptionHandling()
				.accessDeniedHandler(
						(request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN)
				)
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(
						(request, response, authException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN)
				)
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return (web) -> web.ignoring().antMatchers("/swagger-ui/**", "/api-docs/**", "/h2-console/**");
	}
}
