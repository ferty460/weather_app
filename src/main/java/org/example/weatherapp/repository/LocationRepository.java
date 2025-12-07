package org.example.weatherapp.repository;

import org.example.weatherapp.entity.Location;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, Long> {

    List<Location> findAllByUserId(Long userId);

}
