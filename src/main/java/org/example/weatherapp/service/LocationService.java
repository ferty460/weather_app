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
        User user = getUserFromRequest(request);
        List<Location> locations = locationRepository.findAllByUserId(user.getId());

        return locations.stream()
                .map(this::mapToWeatherWithLocationId)
                .toList();
    }

    @Transactional
    public void addToUserList(LocationRequest locationRequest, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        Location location = locationMapper.toEntity(locationRequest, user);

        Location savedLocation = locationRepository.save(location);
        user.addLocation(savedLocation);
    }

    @Transactional
    public void deleteFromUserList(Long id, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " not found"));

        if (!user.getLocations().contains(location)) {
            throw new IllegalArgumentException("Location with id " + id + " not in locations list");
        }

        locationRepository.delete(location);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        String sessionId = WebUtil.getSessionIdFromCookies(request.getCookies());
        return sessionService.getById(sessionId).getUser();
    }

    private WeatherResponse mapToWeatherWithLocationId(Location loc) {
        WeatherResponse weather = apiService.searchWeatherByCoordinates(loc.getLatitude(), loc.getLongitude());
        return new WeatherResponse(loc.getId(), weather.name(), weather.weather(), weather.main());
    }

}
