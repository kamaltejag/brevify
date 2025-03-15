package com.kamalteja.brevify.kafkaService.exception;

import com.kamalteja.brevify.baseService.exception.BaseException;

public class MessagePublishingException extends BaseException {
    private static final String ERROR_CODE = "BV-101";
    private static final String MESSAGE = "Unable to publish message to kafka";

    public MessagePublishingException() {
        super(ERROR_CODE, MESSAGE);
    }
}
