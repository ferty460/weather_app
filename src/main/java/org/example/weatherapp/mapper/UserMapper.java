package org.example.weatherapp.mapper;

import org.example.weatherapp.dto.request.UserRequest;
import org.example.weatherapp.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest userRequest);

}
