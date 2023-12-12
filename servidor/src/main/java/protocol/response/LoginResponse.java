package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginResponse(@NotNull @Valid Payload payload) implements Response<LoginResponse.Payload> {

    public LoginResponse(final String token) {
        this(new Payload(token));
    }

    public record Payload(@NotBlank String token) {
    }
}
