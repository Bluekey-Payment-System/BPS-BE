package com.github.bluekey.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private static final int BEARER_TOKEN_PREFIX = 7;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtProvider.resolveToken(request);
		if (token != null && jwtProvider.validateToken(token)) {
			Authentication authentication = jwtProvider.getAuthentication(token.substring(BEARER_TOKEN_PREFIX));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			Object principal = authentication.getPrincipal();
			Collection<?> authorities = ((UserDetails) principal).getAuthorities();
			SimpleGrantedAuthority authority = (SimpleGrantedAuthority) authorities.stream().findFirst().get();
			if (authority.getAuthority().equals("ROLE_ARTIST")) {
				throw new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
			}
		}

		filterChain.doFilter(request, response);
	}
}
