# Kameleoon React SDK

## Introduction

This library is designed to get weather information in different cities. The library interacts with the api https://openweathermap.org/api to get weather information. You need to register on this resource to get a special access key.

The library supports two mods:
1. Request mode updates weather information only on client requests
2. Polling mode requests new weather information for all saved locations

The library uses caching to have zero delay in response to client requests


## Installation

To install add the following dependency to your pom file
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io/</url>
    </repository>
</repositories>
```

```xml   
<dependency>
    <groupId>com.github.denisdeveloperjava001</groupId>
    <artifactId>WeatherSDK</artifactId>
    <version>259ac378a6</version>
</dependency> 
```

## Usage Example

```java
// example of creating an object to get the weather in London in Polling mode
Weather weatherLondon = new Weather("your token", Mode.BY_POLLING);

// example of creating an object to get the weather in Moscow in Request mode
Weather weatherMoscow = new Weather("your token", Mode.BY_REQUEST);

// example getting weather information
weatherLondon.getWeather();
weatherMoscow.getWeather();

// deactivate object
weatherMoscow.deactivate();
```