package server.entity;

import jakarta.validation.constraints.*;
import protocol.request.AdminCreateUserRequest;

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
) {
        public static UserEntity of(AdminCreateUserRequest.Payload user) {
                return new UserEntity(user.nome(), user.email(), user.senha(), user.tipo(), 1);
        }

}
