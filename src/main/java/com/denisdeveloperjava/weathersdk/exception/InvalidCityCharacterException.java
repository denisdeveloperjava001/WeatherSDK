package com.denisdeveloperjava.weathersdk.exception;

public class InvalidCityCharacterException extends RuntimeException {
    public InvalidCityCharacterException() {
        super("The name of the city must consist only of Latin letters");
    }
}
