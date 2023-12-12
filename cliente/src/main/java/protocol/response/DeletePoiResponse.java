package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DeletePoiResponse(@Valid @NotNull Payload payload) implements Response<DeletePoiResponse.Payload> {
    public DeletePoiResponse(String mensagem) {
        this(new Payload(mensagem));
    }
    public record Payload(@NotNull String mensagem) {}
}
