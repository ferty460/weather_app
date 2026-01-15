package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.LocationKey;
import org.example.weatherapp.dto.request.LocationRequest;
import org.example.weatherapp.dto.response.LocationResponse;
import org.example.weatherapp.dto.response.WeatherResponse;
import org.example.weatherapp.entity.Location;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.LocationMapper;
import org.example.weatherapp.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final OpenWeatherApiService apiService;

    @Transactional(readOnly = true)
    public List<Location> getAllByUserId(Long id) {
        return locationRepository.findAllByUserId(id);
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> searchByName(String query, Long userId) {
        List<LocationResponse> apiLocations = apiService.searchLocationsByName(query);

        return filterDuplicates(apiLocations, userId);
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> filterDuplicates(List<LocationResponse> locations, Long userId) {
        Set<LocationKey> userLocationKeys = getAllByUserId(userId).stream()
                .map(loc -> new LocationKey(
                        loc.getName(),
                        loc.getLatitude(),
                        loc.getLongitude()
                ))
                .collect(Collectors.toSet());

        return locations.stream()
                .filter(loc -> !userLocationKeys.contains(
                        new LocationKey(loc.name(), loc.lat(), loc.lon())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WeatherResponse> getUserLocationsWithWeather(User user) {
        if (user == null) {
            return Collections.emptyList();
        }

        List<Location> locations = getAllByUserId(user.getId());

        return locations.stream()
                .map(this::mapToWeatherWithLocationId)
                .toList();
    }

    @Transactional
    public void addToUserList(LocationRequest locationRequest, User user) {
        Location locToAdd = locationMapper.toEntity(locationRequest, user);
        System.out.printf("Location to add: lat(%s), lon(%s)%n", locToAdd.getLatitude(), locToAdd.getLongitude());

        List<Location> locations = getAllByUserId(user.getId());
        for (Location loc : locations) {
            if (locToAdd.equals(loc)) {
                throw new RuntimeException("Location already exists");
            }
        }

        locationRepository.save(locToAdd);
    }

    @Transactional
    public void deleteFromUserList(Long id, User user) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        List<Location> locations = getAllByUserId(user.getId());
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
