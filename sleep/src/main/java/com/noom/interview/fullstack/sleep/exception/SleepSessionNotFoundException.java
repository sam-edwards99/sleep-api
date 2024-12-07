package com.noom.interview.fullstack.sleep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SleepSessionNotFoundException extends RuntimeException {
    private static final String message = "Sleep session not found.";
     public SleepSessionNotFoundException() {
         super(message);
     }
}
