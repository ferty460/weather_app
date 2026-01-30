package org.example.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.request.LocationSearchRequest;
import org.example.weatherapp.dto.response.LocationResponse;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.interceptor.AuthenticationInterceptor;
import org.example.weatherapp.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OpenWeatherApiController {

    private final LocationService locationService;

    @GetMapping("/locations/search")
    public String searchLocations(
            @Valid @ModelAttribute("query") LocationSearchRequest locationDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Enter a valid location");
            return "redirect:/";
        }

        User user = (User) request.getAttribute(AuthenticationInterceptor.CURRENT_USER_ATTR);
        List<LocationResponse> locations = locationService.searchByName(locationDto.query(), user.getId());

        model.addAttribute("user", user);
        model.addAttribute("locations", locations);
        model.addAttribute("query", locationDto.query());

        return "search_result";
    }

}
