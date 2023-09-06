package com.github.bluekey.config.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bluekey.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String EXCEPTION_PROPERTY = "exception";
    private static final int BEARER_TOKEN_PREFIX = 7;

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        if (token != null && jwtProvider.validateToken(token).equals(JwtValidationType.VALID_JWT)) {
            Authentication authentication = jwtProvider.getAuthentication(token.substring(BEARER_TOKEN_PREFIX));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            request.setAttribute("UnAuthorization", jwtProvider.validateToken(token));
            setErrorMessage(jwtProvider.validateToken(token), request);
        }
        filterChain.doFilter(request, response);
    }

    private void setErrorMessage(JwtValidationType jwtValidationType, HttpServletRequest request) {
        if (jwtValidationType.equals(JwtValidationType.EMPTY_JWT)) {
            request.setAttribute(EXCEPTION_PROPERTY, ErrorCode.EMPTY_JWT);
        }
        if (jwtValidationType.equals(JwtValidationType.EXPIRED_JWT)) {
            request.setAttribute(EXCEPTION_PROPERTY, ErrorCode.EXPIRED_JWT);
        }
        if (jwtValidationType.equals(JwtValidationType.INVALID_JWT)) {
            request.setAttribute(EXCEPTION_PROPERTY, ErrorCode.INVALID_JWT);
        }
        if (jwtValidationType.equals(JwtValidationType.UNSUPPORTED_JWT)) {
            request.setAttribute(EXCEPTION_PROPERTY, ErrorCode.UNSUPPORTED_JWT);
        }
        if(jwtValidationType.equals(JwtValidationType.INVALID_JWT_SIGNATURE)) {
            request.setAttribute(EXCEPTION_PROPERTY, ErrorCode.INVALID_JWT_SIGNATURE);
        }
    }
}
