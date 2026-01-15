package org.example.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.response.LocationResponse;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.interceptor.AuthenticationInterceptor;
import org.example.weatherapp.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final LocationService locationService;

    @GetMapping("/locations/search")
    public String searchLocations(
            @RequestParam("query") @NotBlank String query,
            HttpServletRequest request,
            Model model
    ) {
        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        List<LocationResponse> locations = locationService.searchByName(query, user.getId());

        model.addAttribute("user", user);
        model.addAttribute("locations", locations);
        model.addAttribute("query", query);

        return "search_result";
    }

}
