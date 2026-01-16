package org.example.weatherapp.exception.api;

public class WeatherApiException extends RuntimeException {

    public WeatherApiException(Throwable cause) {
        super("OpenWeather API error", cause);
    }

}
