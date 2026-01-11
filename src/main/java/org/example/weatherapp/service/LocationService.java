package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.LocationRequest;
import org.example.weatherapp.dto.response.WeatherResponse;
import org.example.weatherapp.entity.Location;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.LocationMapper;
import org.example.weatherapp.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final OpenWeatherApiService apiService;

    @Transactional(readOnly = true)
    public List<WeatherResponse> getUserLocationsWithWeather(User user) {
        if (user == null) {
            return Collections.emptyList();
        }

        List<Location> locations = locationRepository.findAllByUserId(user.getId());

        return locations.stream()
                .map(this::mapToWeatherWithLocationId)
                .toList();
    }

    @Transactional
    public void addToUserList(LocationRequest locationRequest, User user) {
        Location location = locationMapper.toEntity(locationRequest, user);
        locationRepository.save(location);
    }

    @Transactional
    public void deleteFromUserList(Long id, User user) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        List<Location> locations = locationRepository.findAllByUserId(user.getId());
        if (!locations.contains(location)) {
            throw new IllegalArgumentException("Location not found");
        }

        locationRepository.delete(location);
    }

    private WeatherResponse mapToWeatherWithLocationId(Location loc) {
        WeatherResponse weather = apiService.searchWeatherByCoordinates(
                loc.getLatitude(), loc.getLongitude()
        );

        return new WeatherResponse(
                loc.getId(),
                weather.name(),
                weather.weather(),
                weather.main()
        );
    }
}
