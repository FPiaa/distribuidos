package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DeletePoiResponse(@Valid @NotNull Payload payload) implements Response<DeletePoiResponse.Payload> {
    public record Payload(@NotNull String mensagem) {}
}
