package com.denisdeveloperjava.weathersdk.exception;

public class NullModeException extends RuntimeException {
    public NullModeException() {
        super("Mode can't be null");
    }
}
