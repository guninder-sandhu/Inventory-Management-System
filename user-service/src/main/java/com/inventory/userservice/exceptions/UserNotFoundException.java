package com.inventory.userservice.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message + " User not found");
    }

    public UserNotFoundException() {
        super("ERROR:User not found ");
    }
}
