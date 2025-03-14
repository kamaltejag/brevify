package com.kamalteja.brevify.shortenerService.enums;

import lombok.Getter;

@Getter
public enum CodeStatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive"),
    EXPIRED("expired");

    private final String status;

    CodeStatusEnum(String status) {
        this.status = status;
    }
}
