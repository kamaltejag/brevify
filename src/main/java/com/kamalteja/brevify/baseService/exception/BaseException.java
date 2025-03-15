package com.kamalteja.brevify.baseService.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class BaseException extends RuntimeException {

    private final String errorCode;
    private List<String> errorFields = Collections.emptyList();

    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, String message, List<String> errorFields) {
        super(message);
        this.errorCode = errorCode;
        this.errorFields = errorFields;
    }
}
