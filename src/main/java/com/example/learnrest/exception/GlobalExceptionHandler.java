package com.example.learnrest.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
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
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Validation Error", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBindException(BindException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Bind Exception", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.UNAUTHORIZED, "Unauthorized", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.CONFLICT, "Conflict", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
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

    @ExceptionHandler(ValidationTokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleValidationTokenEmailExpiredException(
            ValidationTokenExpiredException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Validation Token Expired", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.BAD_REQUEST, "Token not valid", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<Map<String, Object>> handleDataConflictException(DataConflictException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("-", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.CONFLICT, "Duplicate Data", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.NOT_FOUND, "Data tidak ditemukan", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<Map<String, Object>> handleImageUploadException(ImageUploadException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Build the errors array
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        errorResponse.put("errors", buildJsonApiErrors(HttpStatus.NOT_FOUND, "Data tidak ditemukan", errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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