package org.example.weatherapp.mapper;

import org.example.weatherapp.dto.request.LocationRequest;
import org.example.weatherapp.entity.Location;
import org.example.weatherapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "locationRequest.name")
    @Mapping(target = "latitude", source = "locationRequest.lat")
    @Mapping(target = "longitude", source = "locationRequest.lon")
    Location toEntity(LocationRequest locationRequest, User user);

}
