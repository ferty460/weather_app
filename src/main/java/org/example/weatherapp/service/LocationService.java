package org.example.weatherapp.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.response.WeatherResponse;
import org.example.weatherapp.dto.request.LocationRequest;
import org.example.weatherapp.entity.Location;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.LocationMapper;
import org.example.weatherapp.repository.LocationRepository;
import org.example.weatherapp.util.WebUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final SessionService sessionService;
    private final OpenWeatherApiService apiService;

    @Transactional(readOnly = true)
    public List<WeatherResponse> getUserLocationsWithWeather(HttpServletRequest request) {
        String sessionId = WebUtil.getSessionIdFromCookies(request.getCookies());
        User user = sessionService.getById(sessionId).getUser();
        List<Location> locations = locationRepository.findAllByUserId(user.getId());

        return locations.stream()
                .map(this::mapToWeatherWithLocationId)
                .toList();
    }

    @Transactional
    public void add(LocationRequest locationRequest, HttpServletRequest request) {
        String sessionId = WebUtil.getSessionIdFromCookies(request.getCookies());
        User user = sessionService.getById(sessionId).getUser();
        Location location = locationMapper.toEntity(locationRequest, user);

        Location savedLocation = locationRepository.save(location);
        user.addLocation(savedLocation);
    }

    @Transactional
    public void delete(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " not found"));
        locationRepository.delete(location);
    }

    private WeatherResponse mapToWeatherWithLocationId(Location loc) {
        WeatherResponse weather = apiService.getWeatherByCoordinates(loc.getLatitude(), loc.getLongitude());
        return new WeatherResponse(loc.getId(), weather.name(), weather.weather(), weather.main());
    }

}
