package server.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record User(
        @NotBlank
        @Size(min = 3, max = 255)
        String nome,
        @NotBlank
        @Email
        String email,

        boolean isAdmin,
        @Positive
        int id
) { }
