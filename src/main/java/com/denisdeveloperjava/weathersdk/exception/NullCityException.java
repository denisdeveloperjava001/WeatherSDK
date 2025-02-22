package com.denisdeveloperjava.weathersdk.exception;

public class NullCityException extends RuntimeException {
    public NullCityException() {
        super("City can't be null");
    }
}
