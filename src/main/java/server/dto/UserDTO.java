package server.dto;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.*;
import server.entity.User;

public record UserDTO(
        @NotBlank
        @Size(min = 3, max = 255)
        String nome,
        @NotBlank
        @Email
        String email,

        @NotNull
        @SerializedName(value = "tipo")
        Boolean isAdmin,
        @Positive
        @NotNull
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
