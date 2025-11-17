package org.example.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.LocationDto;
import org.example.weatherapp.dto.WeatherDto;
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

    @GetMapping("/locations")
    public List<LocationDto> searchLocations(@RequestParam("query") String location) {
        return apiService.searchLocationsByName(location);
    }

    @GetMapping("/weather")
    public WeatherDto searchWeather(
            @RequestParam("lat") BigDecimal latitude,
            @RequestParam("lon") BigDecimal longitude
    ) {
        return apiService.getWeatherByCoordinates(latitude, longitude);
    }

}
