package com.inventory.orderservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DeletionException extends RuntimeException {
    private final HttpStatus status;

    public DeletionException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
