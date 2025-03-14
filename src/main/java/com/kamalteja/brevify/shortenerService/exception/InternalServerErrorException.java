package com.kamalteja.brevify.shortenerService.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class InternalServerErrorException extends BaseException {
    private static final String ERROR_CODE = "BV-01";
    private static final String MESSAGE = "Internal Server Error";

    public InternalServerErrorException() {
        super(ERROR_CODE, MESSAGE);
    }
}
