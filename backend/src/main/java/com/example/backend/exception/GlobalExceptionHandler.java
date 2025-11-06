package com.example.backend.exception;

import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex)
    {
        Map<String, String> body = Map.of(
            "error", ex.getReason(),
            "status", String.valueOf(ex.getStatusCode().value())
        );
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException ex)
    {
        Map<String, String> body = Map.of(
            "error", "Invalid username or password",
            "satus", "401"
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Map<String, String> body = Map.of(
            "error", "Internal server error"
        );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
}

}
