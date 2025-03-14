package com.kamalteja.brevify.baseService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private T data;

    public static <T> ApiResponse<T> handler(String status, T data) {
        return new ApiResponse<>(status, data);
    }
}
