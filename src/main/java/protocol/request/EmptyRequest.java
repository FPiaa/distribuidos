package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record EmptyRequest(
        @NotNull @Valid Header header,
        Payload payload
) implements Request<EmptyRequest.Payload> {
    public record Payload(){}
}
