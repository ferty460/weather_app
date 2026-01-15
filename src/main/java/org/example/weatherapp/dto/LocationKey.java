package org.example.weatherapp.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class LocationKey {

    private final String name;
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public LocationKey(String name, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationKey other)) return false;

        return Objects.equals(name, other.name)
                && latitude.compareTo(other.latitude) == 0
                && longitude.compareTo(other.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                latitude.stripTrailingZeros(),
                longitude.stripTrailingZeros()
        );
    }
}

