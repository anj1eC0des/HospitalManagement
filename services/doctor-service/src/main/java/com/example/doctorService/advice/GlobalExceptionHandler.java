package com.example.doctorService.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(EntityNotFoundException ex){
        var problemDetail= ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Resource not found.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex){
        var problemDetail= ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid request.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.badRequest().body(problemDetail);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        var pd= ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Type mismatch");
        pd.setDetail("Invalid value for parameter: " + ex.getName());
        pd.setProperty("expectedType", String.valueOf(ex.getRequiredType()));
        pd.setProperty("value", String.valueOf(ex.getValue()));
        pd.setProperty("timestamp", Instant.now());
        return ResponseEntity.badRequest().body(pd);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {
        // Log with stack trace for diagnostics
        log.error("Unhandled exception", ex);
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal server error");
        pd.setDetail("An unexpected error occurred");
        pd.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
}
