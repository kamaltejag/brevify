package com.kamalteja.brevify.baseService.handler;

import com.kamalteja.brevify.baseService.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BaseResponseHandler {
    public static <T> ResponseEntity<ApiResponse<T>> handler(String status, T data) {
        return ResponseEntity.ok(ApiResponse.handler(status, data));
    }
}
