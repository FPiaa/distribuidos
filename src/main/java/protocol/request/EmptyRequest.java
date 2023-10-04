package protocol.request;

import jakarta.validation.Valid;
import protocol.request.header.Header;

public record EmptyRequest(@Valid Header header,
                           Void payload) implements Request<Void> {
}
