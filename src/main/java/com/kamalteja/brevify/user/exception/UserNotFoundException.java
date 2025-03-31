package com.kamalteja.brevify.user.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class UserNotFoundException extends BaseException {
    private static final String ERROR_CODE = "US-02";
    private static final String MESSAGE = "User Not Found";

    public UserNotFoundException() {
        super(ERROR_CODE, MESSAGE);
    }
}
