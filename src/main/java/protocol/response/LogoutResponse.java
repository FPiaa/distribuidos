package protocol.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LogoutResponse(@NotNull Payload payload) implements Response<LogoutResponse.Payload> {

    public LogoutResponse() {
        this(new Payload("desconectado"));
    }

    public record Payload(@NotEmpty String message) {
    }
}
