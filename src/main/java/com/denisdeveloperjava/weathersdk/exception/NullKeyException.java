package com.denisdeveloperjava.weathersdk.exception;

public class NullKeyException extends RuntimeException {
    public NullKeyException() {
        super("Key can't be null");
    }


}
