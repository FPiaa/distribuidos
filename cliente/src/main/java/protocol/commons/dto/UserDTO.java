package protocol.commons.dto;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.*;

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
}
