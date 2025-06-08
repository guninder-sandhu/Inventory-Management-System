package com.inventory.orderservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WrongParameterException extends RuntimeException {
    private final HttpStatus status;

    public WrongParameterException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
