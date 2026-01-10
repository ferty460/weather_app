package org.example.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.entity.User;
import org.example.weatherapp.interceptor.AuthInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        User user = (User) request.getAttribute(AuthInterceptor.CURRENT_USER_ATTR);
        model.addAttribute("user", user);

        return "index";
    }

}
