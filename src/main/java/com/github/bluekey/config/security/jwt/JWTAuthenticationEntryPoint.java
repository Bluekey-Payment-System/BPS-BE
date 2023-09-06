package com.github.bluekey.config.security.jwt;

import com.github.bluekey.exception.ErrorCode;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String EXCEPTION_PROPERTY = "exception";
    private static final String EXCEPTION_CODE_KEY = "code";
    private static final String EXCEPTION_MESSAGE_KEY = "message";


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        ErrorCode errorCode = (ErrorCode) request.getAttribute(EXCEPTION_PROPERTY);

        if (errorCode != null) {
            if (errorCode.equals(ErrorCode.EMPTY_JWT)) {
                setResponse(response, ErrorCode.EMPTY_JWT);
            } else if (errorCode.equals(ErrorCode.UNSUPPORTED_JWT)) {
                setResponse(response, ErrorCode.UNSUPPORTED_JWT);
            } else if (errorCode.equals(ErrorCode.EXPIRED_JWT)) {
                setResponse(response, ErrorCode.EXPIRED_JWT);
            } else if (errorCode.equals(ErrorCode.INVALID_JWT)) {
                setResponse(response, ErrorCode.INVALID_JWT);
            } else if (errorCode.equals(ErrorCode.INVALID_JWT_SIGNATURE)) {
                setResponse(response, ErrorCode.INVALID_JWT_SIGNATURE);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(EXCEPTION_CODE_KEY, errorCode.getCode());
        jsonObject.put(EXCEPTION_MESSAGE_KEY, errorCode.getMessage());
        response.getWriter().print(jsonObject);
    }
}
