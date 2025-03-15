package com.kamalteja.brevify.kafkaService.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

import java.util.List;

public class MessageConsumingException extends BaseException {
    private static final String ERROR_CODE = "BV-102";
    private static final String MESSAGE = "Unable to consume message from kafka";

    public MessageConsumingException(List<String> errorFields) {
        super(ERROR_CODE, MESSAGE, errorFields);
    }
}
