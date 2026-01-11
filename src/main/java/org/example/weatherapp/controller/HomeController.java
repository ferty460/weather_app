package org.example.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.response.WeatherResponse;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.interceptor.AuthenticationInterceptor;
import org.example.weatherapp.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LocationService locationService;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        List<WeatherResponse> locations = locationService.getUserLocationsWithWeather(user);

        model.addAttribute("user", user);
        model.addAttribute("locations", locations);

        return "index";
    }

}
