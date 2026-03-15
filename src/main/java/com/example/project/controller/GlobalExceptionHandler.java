package com.example.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleInvalidTime(DateTimeParseException ex) {
        return ResponseEntity
                .badRequest()
                .body("Invalid time format. Please use 'HH:mm' or 'h:mma' (e.g., 3:00pm).");
    }
}
