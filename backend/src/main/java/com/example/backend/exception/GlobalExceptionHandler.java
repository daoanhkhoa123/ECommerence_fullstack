package com.example.backend.exception;

import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles explicit ResponseStatusException (e.g., forbidden, not found)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex) {
        Map<String, String> body = Map.of(
            "error", ex.getReason(),
            "status", String.valueOf(ex.getStatusCode().value())
        );
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    // Handles authentication errors
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException ex) {
        Map<String, String> body = Map.of(
            "error", "Invalid username or password",
            "status", "401"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // Handles database constraint violations (e.g., duplicate email)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = "Database error";
        if (ex.getCause() != null && ex.getCause().getMessage().contains("accounts_email_key")) {
            message = "Email already exists";
        }
        Map<String, String> body = Map.of(
            "error", message,
            "status", "400"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Fallback generic handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Map<String, String> body = Map.of(
            "error", "Internal server error",
            "status", "500"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
