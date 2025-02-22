package com.denisdeveloperjava.weathersdk.exception;

public class UsedDeactivatedObjectException extends RuntimeException {
    public UsedDeactivatedObjectException() {
        super("Cannot use deactivated object");
    }
}
