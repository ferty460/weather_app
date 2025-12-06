package org.example.weatherapp.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationResponse(
        String name,
        BigDecimal lat,
        BigDecimal lon,
        String country,
        String state
) {
}
