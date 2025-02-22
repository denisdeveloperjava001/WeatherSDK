package com.denisdeveloperjava.weathersdk.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class UnexpectedResponseException extends RuntimeException {
    public UnexpectedResponseException(HttpStatusCode status, String body)  {
        super("Unexpected response " + status.value() + " " + body);
    }

    public UnexpectedResponseException(String errorMessage) {
        super("Unexpected response " + errorMessage);
    }
}
