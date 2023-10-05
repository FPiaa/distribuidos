package server.entity;

import jakarta.validation.constraints.*;

public record UserEntity(
        @NotBlank
        @Size(min = 3, max = 255)
        String nome,
        @NotBlank
        @Email
        String email,

        @NotBlank
        String senha,
        @NotNull
        Boolean isAdmin,
        @Positive
        int id
) { }
