package org.example.weatherapp.repository;

import org.example.weatherapp.entity.Location;

import java.math.BigDecimal;
import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, Long> {

    Optional<Location> findByName(String name);

    Optional<Location> findByCoordinates(BigDecimal latitude, BigDecimal longitude);

}
