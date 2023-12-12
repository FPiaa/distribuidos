package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LogoutResponse(@NotNull @Valid Payload payload) implements Response<LogoutResponse.Payload> {

    public LogoutResponse() {
        this(new Payload("desconectado"));
    }

    public record Payload(@NotNull String mensagem) {
    }
}