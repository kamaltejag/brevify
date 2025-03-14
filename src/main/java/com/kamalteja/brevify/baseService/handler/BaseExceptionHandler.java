package com.kamalteja.brevify.baseService.handler;

import com.kamalteja.brevify.baseService.exception.BaseException;
import com.kamalteja.brevify.baseService.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.kamalteja.brevify.baseService.constants.StringConstants.*;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBaseException(BaseException baseException) {
        Map<String, String> errorData = new LinkedHashMap<>();
        errorData.put(ERROR_CODE, baseException.getErrorCode());
        errorData.put(ERROR_MESSAGE, baseException.getMessage());
        return ResponseEntity.ok().body(ApiResponse.handler(FAILURE, errorData));
    }
}
