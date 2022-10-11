package com.gary.backendv2.exception;

public class AdminAccountExistsException extends RuntimeException {
    public AdminAccountExistsException(String message) {
        super(message);
    }
}
