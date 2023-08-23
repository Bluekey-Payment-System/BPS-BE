package com.github.bluekey.config;

import com.github.bluekey.jwt.JwtAuthenticationFilter;
import com.github.bluekey.jwt.JwtProvider;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SecurityConfig {

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
						config.setAllowedOrigins(List.of("*")); //TODO: front 주소로 변경 필요
						config.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
						return config;
					});
				})
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/api/v1/auth/admin/**", "/api/v1/auth/member/login", "/h2-console/**", "/api/v1/transactions", "/api/v1/transactions/migrate").permitAll()
				.antMatchers("/api/v1/**").hasRole("SUPER_ADMIN")
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling()
				.accessDeniedHandler(
						(request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN)
				)
				.authenticationEntryPoint(
						(request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
				);
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return (web) -> web.ignoring().antMatchers("/swagger-ui/**", "/api-docs/**");
	}
}
