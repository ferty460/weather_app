package org.example.weatherapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "Login is required")
        @Size(min = 3, max = 128, message = "Login must be between 3 and 128 characters")
        String login,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
        String password

) {
}
