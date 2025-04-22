package com.inventory.product.exceptions;

import org.springframework.http.HttpStatus;

public class CreationException extends RuntimeException {
    private final HttpStatus status;

    public CreationException(String message, HttpStatus status) {

        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
