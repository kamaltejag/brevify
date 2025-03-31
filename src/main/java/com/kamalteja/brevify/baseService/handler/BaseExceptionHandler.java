package com.kamalteja.brevify.baseService.handler;

import com.kamalteja.brevify.baseService.exception.BaseException;
import com.kamalteja.brevify.baseService.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.kamalteja.brevify.baseService.constants.StringConstants.*;

@RestControllerAdvice
@Slf4j
public class BaseExceptionHandler {

    /**
     * Handles BaseException by creating a standardized error response.
     * Extracts error code and message from the exception, determines appropriate HTTP status,
     * logs the error details, and returns a formatted API response with failure status.
     *
     * @param baseException The caught BaseException to be handled
     * @return ResponseEntity containing ApiResponse with error details
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBaseException(BaseException baseException) {
        Map<String, String> errorData = new LinkedHashMap<>();
        errorData.put(ERROR_CODE, baseException.getErrorCode());
        errorData.put(ERROR_MESSAGE, baseException.getMessage());

        HttpStatus status = determineHttpStatus(baseException);

        log.error("{} {}",
                Optional.of(baseException).map(BaseException::getErrorCode).orElse(null),
                Optional.of(baseException).map(BaseException::getMessage).orElse(null));

        return ResponseEntity
                .status(status)
                .body(ApiResponse.handler(FAILURE, errorData));
    }

    /**
     * Handles authentication exceptions by creating a standardized error response.
     * Sets a fixed error code (AUTH-01) and message, logs the error details,
     * and returns a formatted API response with UNAUTHORIZED status.
     *
     * @param ex The caught AuthenticationException to be handled
     * @return ResponseEntity containing ApiResponse with error details
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> errorData = new LinkedHashMap<>();
        errorData.put(ERROR_CODE, "AUTH-01");
        errorData.put(ERROR_MESSAGE, "Authentication failed");

        log.error("Authentication failed: {}", Optional.of(ex).map(AuthenticationException::getMessage).orElse(null));

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.handler(FAILURE, errorData));
    }

    /**
     * Handles access denied exceptions by creating a standardized error response.
     * Sets a fixed error code (AUTH-02) and message, logs the error details,
     * and returns a formatted API response with FORBIDDEN status.
     *
     * @param ex The caught AccessDeniedException to be handled
     * @return ResponseEntity containing ApiResponse with error details
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errorData = new LinkedHashMap<>();
        errorData.put(ERROR_CODE, "AUTH-02");
        errorData.put(ERROR_MESSAGE, "Access denied");

        log.error("Access denied: {}", Optional.of(ex).map(AccessDeniedException::getMessage).orElse(null));

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.handler(FAILURE, errorData));
    }

    /**
     * Handles generic exceptions by creating a standardized error response.
     * Sets a fixed error code (BV-00) and message, logs the error details,
     * and returns a formatted API response with INTERNAL_SERVER_ERROR status.
     *
     * @param ex The caught Exception to be handled
     * @return ResponseEntity containing ApiResponse with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleGenericException(Exception ex) {
        Map<String, String> errorData = new LinkedHashMap<>();
        errorData.put(ERROR_CODE, "BV-00");
        errorData.put(ERROR_MESSAGE, "Internal Server Error");

        log.error("Unexpected Error: {}", Optional.of(ex).map(Exception::getMessage).orElse(null));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.handler(FAILURE, errorData));
    }

    private HttpStatus determineHttpStatus(BaseException exception) {
        String errorCode = exception.getErrorCode();

        if (errorCode.startsWith("BV")) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
