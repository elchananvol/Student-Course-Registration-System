package com.system.util;

public class BaseException extends RuntimeException {
    private final int errorCode;

    public BaseException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
