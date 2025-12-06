package org.example.weatherapp.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.LocationRequest;
import org.example.weatherapp.entity.Location;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.LocationMapper;
import org.example.weatherapp.repository.LocationRepository;
import org.example.weatherapp.util.WebUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final SessionService sessionService;

    @Transactional
    public void save(LocationRequest locationRequest, HttpServletRequest request) {
        String sessionId = WebUtil.getSessionIdFromCookies(request.getCookies());
        User user = sessionService.getUserBySessionId(sessionId);
        Location location = locationMapper.toEntity(locationRequest, user);

        Location savedLocation = locationRepository.save(location);
        user.addLocation(savedLocation);
    }

}
