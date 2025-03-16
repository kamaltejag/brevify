package com.kamalteja.brevify.shortenerService.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class ShortUrlCreationFailedException extends BaseException {
    private static final String ERROR_CODE = "BV-05";
    private static final String MESSAGE = "Unable to generate short url";

    public ShortUrlCreationFailedException() {
        super(ERROR_CODE, MESSAGE);
    }
}
