package com.denisdeveloperjava.weathersdk.model;

public class CachedResponse {

    private final String response;
    private final long unixTime;

    public CachedResponse(String response) {
        this.response = response;
        this.unixTime = System.currentTimeMillis();
    }

    public String getResponse() {
        return response;
    }

    public long getUnixTime() {
        return unixTime;
    }
}
