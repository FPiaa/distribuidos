package server.dto;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.*;
import server.entity.User;

public record UserDTO(
        @NotBlank(message = "nome não pode ser nulo")
        @Size(min = 3, max = 255)
        String nome,
        @NotBlank
        @Email(message = "email não pode ser nulo")
        String email,

        @NotNull(message = "tipo não pode ser nulo")
        @SerializedName(value = "tipo")
        Boolean isAdmin,
        @Positive(message = "id deve ser positivo")
        @SerializedName(value = "registro")
        Long id
) {
    public static UserDTO of(User userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserDTO(userEntity.getNome(), userEntity.getEmail(), userEntity.getIsAdmin(), userEntity.getId());
    }
}
