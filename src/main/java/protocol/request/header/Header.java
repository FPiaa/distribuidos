package protocol.request.header;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Header(
        @NotBlank String operation,

        @Pattern(regexp = "(?:\\w|-){2,}(?:\\.(?:\\w|-){2,}){2}")
        String token) {
}
