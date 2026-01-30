package org.example.weatherapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LocationSearchRequest(

        @NotBlank(message = "Введите название города")
        String query

) {
}
