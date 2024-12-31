package com.system.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class UserBaseException extends RuntimeException {
    private final HttpStatusCode errorCode;

    public UserBaseException(String message, HttpStatusCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
