package com.kamalteja.brevify.user.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class AuthenticationFailedException extends BaseException {
    private static final String ERROR_CODE = "US-02";
    private static final String MESSAGE = "Unable to authenticate user";

    public AuthenticationFailedException() {
        super(ERROR_CODE, MESSAGE);
    }
}
