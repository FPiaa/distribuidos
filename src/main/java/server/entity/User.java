package server.entity;

import com.google.gson.annotations.Expose;
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

        @NotBlank
        @Expose(serialize = false)
        String senha,

        boolean isAdmin,
        @Positive
        int id
) { }
