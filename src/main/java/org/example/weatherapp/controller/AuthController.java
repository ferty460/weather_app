package org.example.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.UserDto;
import org.example.weatherapp.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto) {
        return authService.register(userDto);
    }

}
