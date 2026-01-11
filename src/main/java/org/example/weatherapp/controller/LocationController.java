package org.example.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.response.WeatherResponse;
import org.example.weatherapp.dto.request.LocationRequest;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.interceptor.AuthenticationInterceptor;
import org.example.weatherapp.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<WeatherResponse> getLocations(HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        return locationService.getUserLocationsWithWeather(user);
    }

    @PostMapping
    public void addLocation(@RequestBody LocationRequest locationRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        locationService.addToUserList(locationRequest, user);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable("id") Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        locationService.deleteFromUserList(id, user);
    }

}
