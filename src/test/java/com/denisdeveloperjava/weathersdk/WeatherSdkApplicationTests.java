package com.denisdeveloperjava.weathersdk;
import com.denisdeveloperjava.weathersdk.exception.*;
import com.denisdeveloperjava.weathersdk.model.Mode;
import com.denisdeveloperjava.weathersdk.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class WeatherSdkApplicationTests {

    private final String API_KEY = "b86d281debb5431b4482ad6cfceb00da";
    private final String LONDON = "London";


    @Test
    void notNullKeyTest() {
        assertThrows(NullKeyException.class, () -> new Weather(null, Mode.BY_POLLING));
    }

    @Test
    void notNullModeTest() {
        assertThrows(NullModeException.class, () -> new Weather(API_KEY, null));
    }

    @Test
    void notUseKeyTest() {
        Weather weather = new Weather(API_KEY, Mode.BY_REQUEST);
        assertThrows(AlreadyUsedKeyException.class, () -> new Weather(API_KEY, Mode.BY_REQUEST));
        weather.deactivate();
    }

    @Test
    void validCityTest() {
        Weather weather = new Weather(API_KEY, Mode.BY_REQUEST);
        mockApiCall(weather);
        assertThrows(NullCityException.class, () -> weather.getWeather(null));
        assertThrows(InvalidCityCharacterException.class, () -> weather.getWeather("!@#$%%^&*()123465"));
        weather.deactivate();
    }

    @Test
    void invalidKeyTest() {
        Weather weather = new Weather("0000****", Mode.BY_REQUEST);
        mockBadTokenApiCall(weather);
        assertThrows(UnexpectedResponseException.class, () -> weather.getWeather(LONDON));
        weather.deactivate();
    }

    @Test
    void deactivatedObjectTest() {
        Weather weather = new Weather(API_KEY, Mode.BY_REQUEST);
        mockApiCall(weather);
        weather.deactivate();
        assertThrows(UsedDeactivatedObjectException.class, () -> weather.getWeather(LONDON));
        weather.deactivate();
    }

    @Test
    void correctRequestWithByRequestModeTest() {
        Weather weather = new Weather(API_KEY, Mode.BY_REQUEST);
        mockApiCall(weather);
        assertNotNull(weather.getWeather(LONDON));
        weather.deactivate();
    }

    @Test
    void correctRequestWithByPollingModeTest() {
        Weather weather = new Weather(API_KEY, Mode.BY_POLLING);
        mockApiCall(weather);
        assertNotNull(weather.getWeather(LONDON));
        weather.deactivate();
    }

    @Test
    void correctCacheTest() {
        Weather weather = new Weather(API_KEY, Mode.BY_REQUEST);
        mockApiCall(weather);
        weather.getWeather(LONDON);
        assertTrue(Cache.getCacheCityWeather().containsKey(LONDON));
        weather.deactivate();
    }

    @Test
    void correctSizeCacheTest() {
        Weather weather = new Weather(API_KEY, Mode.BY_REQUEST);
        mockApiCall(weather);
        weather.getWeather(LONDON);
        weather.getWeather("Moscow");
        weather.getWeather("Bangkok");
        weather.getWeather("Minsk");
        weather.getWeather("Paris");
        weather.getWeather("Amsterdam");
        weather.getWeather("Berlin");
        weather.getWeather("Atlanta");
        weather.getWeather("Chicago");
        weather.getWeather("Los Angeles");
        weather.getWeather("Dubai");
        assertEquals(Cache.getCacheCityWeather().size() , 10);
        assertFalse(Cache.getCacheCityWeather().containsKey(LONDON));
        weather.deactivate();
    }

    private void mockApiCall(Weather weather) {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok("some weather"));
        weather.setRestTemplate(restTemplate);
    }

    private void mockBadTokenApiCall(Weather weather) {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.status(409).body(""));
        weather.setRestTemplate(restTemplate);
    }
}
