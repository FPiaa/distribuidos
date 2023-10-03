package protocol.request;

import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record EmptyRequest(@NotNull Header header,
                           EmptyRequest.Payload payload) implements Request<EmptyRequest.Payload> {
    public static class Payload {
    }
}
