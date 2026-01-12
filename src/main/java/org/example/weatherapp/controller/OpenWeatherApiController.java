package org.example.weatherapp.controller;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.response.LocationResponse;
import org.example.weatherapp.dto.response.WeatherResponse;
import org.example.weatherapp.service.OpenWeatherApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final OpenWeatherApiService apiService;

    @GetMapping("/locations/search")
    public String searchLocations(@RequestParam("query") @NotBlank String location, Model model) {
        List<LocationResponse> locations = apiService.searchLocationsByName(location);
        model.addAttribute("locations", locations);
        model.addAttribute("query", location);

        return "search_result";
    }

    @GetMapping("/weather")
    public WeatherResponse searchWeather(
            @RequestParam("lat") @NotNull @DecimalMin("-90.0") @DecimalMax("90") BigDecimal latitude,
            @RequestParam("lon") @NotNull @DecimalMin("-180") @DecimalMax("180") BigDecimal longitude
    ) {
        return apiService.searchWeatherByCoordinates(latitude, longitude);
    }

}
