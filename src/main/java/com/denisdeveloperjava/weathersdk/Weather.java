package com.denisdeveloperjava.weathersdk;

import com.denisdeveloperjava.weathersdk.exception.*;
import com.denisdeveloperjava.weathersdk.exception.*;
import com.denisdeveloperjava.weathersdk.model.CachedResponse;
import com.denisdeveloperjava.weathersdk.model.Mode;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import java.util.concurrent.*;

/**
 * the class is designed to get the weather in the city
 * @author Usoltsev Denis
 * @version 1.0.2
 */

public class Weather {

    private final Mode mode;
    private final String key;
    private boolean isActivated = true;
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * @param key key to access the API from the <a href="https://openweathermap.org/api">resource</a>
     * @param mode mode of behaviour
     *             BY_REQUEST mode - the SDK updates weather information only when requested by clients.
     *             BY_POLLING mode - the SDK requests new weather information for all stored locations periodically.
     * @throws NullKeyException if key is null
     * @throws NullModeException if mode is null
     * @throws AlreadyUsedKeyException if key is already used
     */

    public Weather(String key, Mode mode) {
        if(key == null) {
            throw new NullKeyException();
        }
        if(mode == null) {
            throw new NullModeException();
        }
        if(Cache.getUsedApiKeys().contains(key)) {
            throw new AlreadyUsedKeyException();
        }

        Cache.getUsedApiKeys().add(key);
        this.key = key;
        this.mode = mode;

        if(mode == Mode.BY_POLLING) {
            if (Cache.getCacheUpdateExecutor() == null) {
                ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
                executorService.scheduleWithFixedDelay(() -> {
                    for (Map.Entry<String, CachedResponse> entry : Cache.getCacheCityWeather().entrySet()) {
                        Cache.getCacheCityWeather().put(entry.getKey(), new CachedResponse(getWeatherApiCall(entry.getKey())));
                    }
                }, 0, 60, TimeUnit.SECONDS);
                Cache.setCacheUpdateExecutor(executorService);
            }
        }
    }

    /**
     * Used to get the weather
     * @param city the name of the city.
     *             If there are several cities with the same name,
     *             information about one of them will be given
     * @return weather information in a given city in JSON format
     * @throws UsedDeactivatedObjectException if a deactivated object is used
     * @throws NullCityException if city is null
     * @throws InvalidCityCharacterException if the city name is not in the correct format
     *
     */

    public String getWeather(String city) {
        if(!isActivated) {
            throw new UsedDeactivatedObjectException();
        }
        if(city == null) {
            throw new NullCityException();
        }
        if(!Pattern.compile("^[A-Za-z ]+$").matcher(city).matches()) {
            throw new InvalidCityCharacterException();
        }

        if(mode == Mode.BY_REQUEST) {
            if(Cache.getCacheCityWeather().containsKey(city)) {
                long cachedTime = Cache.getCacheCityWeather().get(city).getUnixTime();
                long timeNow = System.currentTimeMillis();
                if(timeNow - cachedTime >= 600000) {
                    String responseBody = getWeatherApiCall(city);
                    addCityToCache(city, responseBody);
                    return responseBody;
                } else {
                    return Cache.getCacheCityWeather().get(city).getResponse();
                }
            } else {
                String responseBody = getWeatherApiCall(city);
                addCityToCache(city, responseBody);
                return responseBody;
            }
        } else if(mode == Mode.BY_POLLING) {
            if(Cache.getCacheCityWeather().containsKey(city)) {
                return Cache.getCacheCityWeather().get(city).getResponse();
            } else {
                String responseBody = getWeatherApiCall(city);
                addCityToCache(city, responseBody);
                return responseBody;
            }
        } else {
            throw new UnknownModeException();
        }
    }

    /**
     * Deactivates the object, releases the API key
     */
    public void deactivate() {
        if(Cache.getUsedApiKeys().size() == 1) {
            if (mode == Mode.BY_POLLING) {
                Cache.getCacheUpdateExecutor().shutdownNow();
            }
            Cache.getCacheCityWeather().clear();
        }
        Cache.getUsedApiKeys().remove(key);
        isActivated = false;
    }

    private String getWeatherApiCall(String city) {
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key,
                    HttpEntity.EMPTY, String.class);
        } catch(Exception e) {
            throw new UnexpectedResponseException(e.getMessage());
        }

        if(response.getStatusCode().value() != 200) {
            throw new UnexpectedResponseException(response.getStatusCode(), response.getBody());
        }

        return response.getBody();
    }

    private void addCityToCache(String city, String weather) {
        if(Cache.getCacheCityWeather().size() >= 10) {
            Cache.getCacheCityWeather().remove(Cache.getCacheCityWeather().keySet().iterator().next());
        }
        Cache.getCacheCityWeather().put(city, new CachedResponse(weather));
    }

    protected void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
