package com.denisdeveloperjava.weathersdk.model;

/**
 * Mode of behaviour
 * BY_REQUEST mode - the SDK updates weather information only when requested by clients.
 * BY_POLLING mode - the SDK requests new weather information for all stored locations periodically.
 */
public enum Mode {
    BY_REQUEST, BY_POLLING
}
