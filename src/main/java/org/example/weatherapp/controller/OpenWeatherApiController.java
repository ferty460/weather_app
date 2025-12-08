package org.example.weatherapp.controller;

import jakarta.validation.constraints.*;
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
    public List<LocationResponse> searchLocations(@RequestParam("query") @NotBlank String location) {
        return apiService.searchLocationsByName(location);
    }

    @GetMapping("/weather")
    public WeatherResponse searchWeather(
            @RequestParam("lat") @NotNull @DecimalMin("-90.0") @DecimalMax("90") BigDecimal latitude,
            @RequestParam("lon") @NotNull @DecimalMin("-180") @DecimalMax("180") BigDecimal longitude
    ) {
        return apiService.searchWeatherByCoordinates(latitude, longitude);
    }

}
