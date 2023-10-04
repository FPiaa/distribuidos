package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record LogoutRequest(
        @NotNull
        @Valid
        Header header,
        Payload payload
) implements Request<LogoutRequest.Payload> {
    public LogoutRequest(final String userToken) {
        this(new Header(RequisitionOperations.LOGOUT, userToken), null);
    }

    public record Payload() {
    }

}
