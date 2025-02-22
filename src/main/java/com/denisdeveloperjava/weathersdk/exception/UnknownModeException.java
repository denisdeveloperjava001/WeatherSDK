package com.denisdeveloperjava.weathersdk.exception;

public class UnknownModeException extends RuntimeException {
    public UnknownModeException() {
        super("Unknown mode");
    }
}
