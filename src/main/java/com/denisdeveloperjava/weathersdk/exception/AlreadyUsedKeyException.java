package com.denisdeveloperjava.weathersdk.exception;

public class AlreadyUsedKeyException extends RuntimeException {
    public AlreadyUsedKeyException() {
        super("This API key is already in use");
    }
}
