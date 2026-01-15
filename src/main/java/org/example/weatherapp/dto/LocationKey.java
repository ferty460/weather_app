package org.example.weatherapp.dto;

import java.math.BigDecimal;

public record LocationKey(
        String name,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
