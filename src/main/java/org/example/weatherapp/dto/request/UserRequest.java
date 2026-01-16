package org.example.weatherapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 64, message = "Username must be between 3 and 64 characters")
        String login,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
        String password

) {
}
