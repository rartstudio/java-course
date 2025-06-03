package com.example.learnrest.exception;

public class ValidationTokenExpiredException extends RuntimeException {
    public ValidationTokenExpiredException(String message) {
        super(message);
    }
}
