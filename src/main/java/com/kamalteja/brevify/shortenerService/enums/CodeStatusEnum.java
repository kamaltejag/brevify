package com.kamalteja.brevify.shortenerService.enums;

public enum CodeStatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive"),
    EXPIRED("expired");

    private final String status;

    CodeStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
