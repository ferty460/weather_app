package org.example.weatherapp.dto.request;

import java.math.BigDecimal;

public record LocationRequest(
        String name,
        BigDecimal lat,
        BigDecimal lon
) {
}
