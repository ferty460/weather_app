package org.example.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.LocationDto;
import org.example.weatherapp.service.OpenWeatherApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final OpenWeatherApiService apiService;

    @GetMapping
    public List<LocationDto> searchLocation(@RequestParam("search") String location) {
        return apiService.searchLocationsByName(location);
    }

}
