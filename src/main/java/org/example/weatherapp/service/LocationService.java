package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.Location;
import org.example.weatherapp.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

}
