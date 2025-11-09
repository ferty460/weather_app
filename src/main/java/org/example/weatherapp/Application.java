package org.example.weatherapp;

import org.example.weatherapp.config.AppConfig;
import org.example.weatherapp.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService userService = context.getBean(UserService.class);

            System.out.println(userService.getAll());
        }
    }

}
