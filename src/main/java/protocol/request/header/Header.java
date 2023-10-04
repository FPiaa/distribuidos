package protocol.request.header;

import jakarta.validation.constraints.NotBlank;

public record Header(@NotBlank String operation, String token) {
}
