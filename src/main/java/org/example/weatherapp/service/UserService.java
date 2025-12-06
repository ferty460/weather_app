package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.UserDto;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.UserMapper;
import org.example.weatherapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User with login " + login + " not found"));
    }

    public UserDto create(User user) {
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

}
