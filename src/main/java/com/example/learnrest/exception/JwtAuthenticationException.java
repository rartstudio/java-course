package com.example.learnrest.exception;

import org.springframework.http.HttpStatus;

public class JwtAuthenticationException extends RuntimeException {
    private final HttpStatus status;

    public JwtAuthenticationException(String message) {
        this(message, HttpStatus.UNAUTHORIZED);
    }

    public JwtAuthenticationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
