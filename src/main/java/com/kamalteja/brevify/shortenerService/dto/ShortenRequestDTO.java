package com.kamalteja.brevify.shortenerService.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenRequestDTO(
        @NotBlank(message = "URL cannot be blank")
        String url
) {
}
