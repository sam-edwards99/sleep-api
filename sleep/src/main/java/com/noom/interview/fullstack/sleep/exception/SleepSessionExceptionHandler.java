package com.noom.interview.fullstack.sleep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SleepSessionExceptionHandler {

    @ExceptionHandler({SleepSessionNotFoundException.class, SleepSessionNotFoundInLastThirtyDaysException.class})
    public ResponseEntity<ErrorResponse> handle(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

