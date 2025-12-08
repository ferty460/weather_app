package org.example.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.response.LocationResponse;
import org.example.weatherapp.dto.response.WeatherResponse;
import org.example.weatherapp.service.OpenWeatherApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final OpenWeatherApiService apiService;

    @GetMapping("/locations/search")
    public List<LocationResponse> searchLocations(@RequestParam("query") String location) {
        return apiService.searchLocationsByName(location);
    }

    @GetMapping("/weather")
    public WeatherResponse searchWeather(
            @RequestParam("lat") BigDecimal latitude,
            @RequestParam("lon") BigDecimal longitude
    ) {
        return apiService.getWeatherByCoordinates(latitude, longitude);
    }

}
