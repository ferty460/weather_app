package org.example.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.LocationRequest;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.interceptor.AuthenticationInterceptor;
import org.example.weatherapp.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public String addLocation(@ModelAttribute LocationRequest locationRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        locationService.addToUserList(locationRequest, user);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @DeleteMapping("/{id}")
    public String deleteLocation(@PathVariable("id") Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        locationService.deleteFromUserList(id, user);

        return "redirect:/";
    }

}
