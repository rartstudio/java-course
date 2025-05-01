package com.example.learnrest.exception;

public class ValidationTokenEmailExpiredException extends RuntimeException {
  public ValidationTokenEmailExpiredException(String message) {
    super(message);
  }
}
