package org.example.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.UserRequest;
import org.example.weatherapp.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Valid UserRequest userRequest, HttpServletResponse response) {
        authService.login(userRequest, response);

        return "redirect:/";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute @Valid UserRequest userRequest) {
        authService.register(userRequest);

        return "redirect:/auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);

        return "redirect:/auth/login";
    }

}
