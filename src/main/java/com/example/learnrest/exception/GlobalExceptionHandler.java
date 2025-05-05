package com.example.learnrest.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Validation Error", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> 
            errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Constraint Violation", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(DuplicateEmailException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Email", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Credential", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationTokenEmailExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleValidationTokenEmailExpiredException(ValidationTokenEmailExpiredException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Validation Token Email", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleJwtAuthenticationException(JwtAuthenticationException ex) {
      Map<String, Object> errorResponse = new HashMap<>();
      
      Map<String, String> errors = new HashMap<>();
      errors.put("authorization", ex.getMessage());

      errorResponse.put("errors", buildJsonApiErrors(ex.getStatus(), "JWT Authentication Failed", errors));
      return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    private Object[] buildJsonApiErrors(HttpStatus status, String title, Map<String, String> errors) {
        return errors.entrySet().stream()
            .map(entry -> {
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("status", String.valueOf(status.value()));
                errorDetails.put("title", title);
                errorDetails.put("detail", entry.getValue());
                errorDetails.put("source", Map.of("pointer", entry.getKey()));
                return errorDetails;
            })
            .toArray();
    }
}