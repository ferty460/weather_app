package org.example.weatherapp.exception.api;

import java.math.BigDecimal;

public class WeatherNotFoundException extends RuntimeException {

    public WeatherNotFoundException(BigDecimal lat, BigDecimal lon) {
        super("The weather could not be found by coords: (%s, %s)".formatted(lat, lon));
    }

}
