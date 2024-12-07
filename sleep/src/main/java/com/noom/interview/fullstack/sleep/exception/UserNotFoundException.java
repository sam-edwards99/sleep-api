package com.noom.interview.fullstack.sleep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private static final String message = "User does not exist";

    public UserNotFoundException() {
        super(message);
    }


}
