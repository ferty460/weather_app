package org.example.weatherapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.LocationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    private static final String API_URL = "https://api.openweathermap.org/geo/1.0/direct";
    private static final String FIND_BY_NAME_URL = API_URL + "?q=%s&limit=5&appid=%s&units=metric";

    @Value("${open-weather.api.key}")
    private String apiKey;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    @Transactional
    public List<LocationDto> searchLocationsByName(String name) {
        String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String url = FIND_BY_NAME_URL.formatted(encoded, apiKey);

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
            String jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            List<LocationDto> locationDtoList = mapper.readValue(jsonResponse, new TypeReference<>() {});

            if (locationDtoList.isEmpty()) {
                throw new RuntimeException("The location could not be found"); // todo: custom exception
            }

            return locationDtoList;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("URI to find locations failed", e); // todo: custom exception
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e); // todo: custom exception
        }
    }

}
