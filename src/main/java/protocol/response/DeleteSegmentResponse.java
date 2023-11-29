package protocol.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DeleteSegmentResponse(@Valid @NotNull Payload payload) implements Response<DeleteSegmentResponse.Payload> {
    public record Payload(@NotNull String mensagem) {}
}
