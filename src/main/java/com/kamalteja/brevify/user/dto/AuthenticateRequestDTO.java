package com.kamalteja.brevify.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateRequestDTO(
        @NotBlank(message = "username cannot be blank")
        String username,
        @NotBlank(message = "password cannot be blank")
        String password
) {
}
