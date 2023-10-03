package protocol.request.header;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record Header(@NotBlank String operation, String token) {
}
