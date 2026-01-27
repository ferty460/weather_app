package org.example.weatherapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.weatherapp.dto.response.LocationResponse;
import org.example.weatherapp.exception.api.WeatherApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherApiServiceTest {

    private static final String JSON_RESPONSE = """
            [
              {"name":"Moscow","lat":55.7504461,"lon":37.6174943},
              {"name":"Saint Petersburg","lat":59.9342802,"lon":30.3350986}
            ]
            """;

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private OpenWeatherApiService openWeatherApiService;

    @Test
    void searchLocationsByName_returnsParsedLocations() throws IOException, InterruptedException {
        when(httpResponse.body()).thenReturn(JSON_RESPONSE);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        List<LocationResponse> expectedLocations = List.of(
                new LocationResponse("Moscow", BigDecimal.ZERO, BigDecimal.ONE, "RU", ""),
                new LocationResponse("Saint Petersburg", BigDecimal.ONE, BigDecimal.ZERO, "", "")
        );

        when(mapper.readValue(eq(JSON_RESPONSE), any(TypeReference.class)))
                .thenReturn(expectedLocations);

        List<LocationResponse> locations = openWeatherApiService.searchLocationsByName("Moscow");

        assertEquals(2, locations.size());
        assertEquals("Moscow", locations.get(0).name());
        assertEquals("Saint Petersburg", locations.get(1).name());
    }

    @Test
    void searchLocationsByName_whenHttpClientThrowsIOException_thenThrowsWeatherApiException() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("OpenWeather API error"));

        assertThatThrownBy(() -> openWeatherApiService.searchLocationsByName("Moscow"))
                .isInstanceOf(WeatherApiException.class)
                .hasMessageContaining("OpenWeather API error");
    }

    @Test
    void searchLocationsByName_whenMapperThrowsJsonProcessingException_thenThrowsWeatherApiException() throws IOException, InterruptedException {
        when(httpResponse.body()).thenReturn("invalid json");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(mapper.readValue(anyString(), any(TypeReference.class)))
                .thenThrow(new JsonProcessingException("OpenWeather API error") {});

        assertThatThrownBy(() -> openWeatherApiService.searchLocationsByName("Moscow"))
                .isInstanceOf(WeatherApiException.class)
                .hasMessageContaining("OpenWeather API error");
    }

    @Test
    void searchLocationsByName_whenEmptyResponse_thenReturnsEmptyList() throws IOException, InterruptedException {
        when(httpResponse.body()).thenReturn("[]");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(mapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<LocationResponse> locations = openWeatherApiService.searchLocationsByName("Moscow");
        assertThat(locations).isEmpty();
    }

}
