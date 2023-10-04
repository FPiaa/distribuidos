package protocol.request.header;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Header(
        @NotBlank String operation,

        @Pattern(regexp = "(\\w|\\d)+\\.(\\w|\\d)+\\.(\\w|\\d)+$")
        String token) {
}
