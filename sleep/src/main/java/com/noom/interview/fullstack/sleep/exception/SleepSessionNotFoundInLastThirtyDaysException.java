package com.noom.interview.fullstack.sleep.exception;

public class SleepSessionNotFoundInLastThirtyDaysException extends RuntimeException {
    private static final String message = "No sleep sessions found in last thirty days";
    public SleepSessionNotFoundInLastThirtyDaysException() {
        super(message);
    }
}
