package protocol.request;

import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record LoginRequest(@NotNull Header header,
                           LoginRequest.@NotNull Payload payload) implements Request<LoginRequest.Payload> {

    public LoginRequest(@NotNull String email, @NotNull String password) {
        this(new Header(RequisitionOperations.LOGIN, null), new Payload(email, password));
    }

    public record Payload(@NotNull String email, @NotNull String password) {
    }
}

