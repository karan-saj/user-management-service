package com.bank.userManagement.exception;

public class InvalidUserDetailsException extends RuntimeException {

    public InvalidUserDetailsException(String message) {
        super(message);
    }

    public InvalidUserDetailsException(String message, Throwable cause) {
        super(message, cause);
    }
}
