package com.kamalteja.brevify.user.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class UserExistsException extends BaseException {
    private static final String ERROR_CODE = "US-01";
    private static final String MESSAGE = "User Already exists";

    public UserExistsException() {
        super(ERROR_CODE, MESSAGE);
    }
}
