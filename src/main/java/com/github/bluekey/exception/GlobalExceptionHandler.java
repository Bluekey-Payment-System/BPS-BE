package com.github.bluekey.exception;

import com.github.bluekey.exception.transaction.ExcelUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED);
        log.debug("Authentication failed: {}", ex.getDetail());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.AUTHORIZATION_FAILED);
        log.debug("Request does not have authorization: {}", ex.getDetail());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_TYPE);
        log.debug("Parameter type is invalid: {}", ex.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED);
        log.debug("Authentication not allowed: {}", ex.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : ""));

        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
        log.debug("Argument validation has failed: {}", ex.getMessage());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), ex.getDetail());
        log.error("Business exception has occurred: {}", ex.getDetail(), ex);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ExcelUploadException.class)
    public ResponseEntity<UploadErrorResponse> handleExcelUploadException(ExcelUploadException ex) {
        UploadErrorResponse response = UploadErrorResponse.of(ex.getErrorCode(), ex.getErrors());
        return new ResponseEntity<>(response, response.getStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptionHandler(Exception ex) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
