package org.example.weatherapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.LocationDto;
import org.example.weatherapp.dto.WeatherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenWeatherApiService {

    private static final String API_URL = "https://api.openweathermap.org/";
    private static final String FIND_BY_NAME_URL = API_URL + "geo/1.0/direct?q=%s&limit=%s&appid=%s&units=metric";
    private static final String FIND_BY_COORDS_URL = API_URL + "data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric";
    private static final int LOCATIONS_LIMIT = 5;

    @Value("${open-weather.api.key}")
    private String apiKey;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    // todo: name validation
    // todo: custom exception
    @Transactional
    public List<LocationDto> searchLocationsByName(String name) {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String url = FIND_BY_NAME_URL.formatted(encodedName, LOCATIONS_LIMIT, apiKey);

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
            String jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            List<LocationDto> locationDtoList = mapper.readValue(jsonResponse, new TypeReference<>() {});

            if (locationDtoList.isEmpty()) {
                throw new RuntimeException("The location by name could not be found by name: " + name);
            }

            return locationDtoList;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("URI to find locations with name " + name + " failed", e);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public WeatherDto getWeatherByCoordinates(BigDecimal lat, BigDecimal lon) {
        String url = FIND_BY_COORDS_URL.formatted(lat, lon, apiKey);

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
            String jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            WeatherDto weatherDto = mapper.readValue(jsonResponse, new TypeReference<>() {});

            if (weatherDto == null) {
                throw new RuntimeException("The weather could not be found by coords: (%s, %s)".formatted(lat, lon));
            }

            return weatherDto;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("URI to find locations with coords (%s, %s) failed".formatted(lat, lon), e);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
