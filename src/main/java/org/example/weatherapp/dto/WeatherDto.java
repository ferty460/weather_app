package org.example.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("weather") List<Weather> weather,
        @JsonProperty("main") Main main
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Weather(
            @JsonProperty("description") String description,
            @JsonProperty("icon") String icon
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Main(
            @JsonProperty("temp") BigDecimal temp,
            @JsonProperty("feels_like") BigDecimal feelsLike,
            @JsonProperty("humidity") BigDecimal humidity
    ) {}

}
