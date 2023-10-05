package server.dto;

import jakarta.validation.constraints.*;
import server.entity.UserEntity;

public record UserDTO(
        @NotBlank
        @Size(min = 3, max = 255)
        String nome,
        @NotBlank
        @Email
        String email,

        @NotNull
        Boolean isAdmin,
        @Positive
        int id
) {
    public static UserDTO of(UserEntity userEntity) {
        return new UserDTO(userEntity.nome(), userEntity.email(), userEntity.isAdmin(), userEntity.id());
    }
}
