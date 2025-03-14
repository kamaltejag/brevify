package com.kamalteja.brevify.shortenerService.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class CodeNotFoundException extends BaseException {
    private static final String ERROR_CODE = "BV-03";
    private static final String MESSAGE = "Short URL was not Found";

    public CodeNotFoundException() {
        super(ERROR_CODE, MESSAGE);
    }
}
