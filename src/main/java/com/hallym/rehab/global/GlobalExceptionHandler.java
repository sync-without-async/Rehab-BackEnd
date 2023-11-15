package com.hallym.rehab.global;

import com.hallym.rehab.global.exception.IncorrectPasswordException;
import com.hallym.rehab.global.exception.MidExistsException;
import com.hallym.rehab.global.exception.StaffNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StaffNotFoundException.class)
    public ResponseEntity<String> handleStaffNotFoundException(StaffNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MidExistsException.class)
    public ResponseEntity<String> handleMidExistsException(MidExistsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPasswordException(IncorrectPasswordException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
