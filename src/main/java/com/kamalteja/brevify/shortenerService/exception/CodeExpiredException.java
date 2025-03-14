package com.kamalteja.brevify.shortenerService.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class CodeExpiredException extends BaseException {
    private static final String ERROR_CODE = "BV-02";
    private static final String MESSAGE = "Short URL has Expired";

    public CodeExpiredException() {
        super(ERROR_CODE, MESSAGE);
    }
}
