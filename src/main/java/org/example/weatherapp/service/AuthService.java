package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.UserDto;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.mapper.UserMapper;
import org.example.weatherapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // todo: validation
    @Transactional
    public UserDto register(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));

        return userMapper.toDto(userRepository.save(user));
    }

}
