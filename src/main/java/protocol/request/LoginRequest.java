package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record LoginRequest(@NotNull @Valid Header header,
                           @NotNull @Valid Payload payload) implements Request<LoginRequest.Payload> {

    public LoginRequest(final @NotEmpty String email, final @NotEmpty String password) {
        this(new Header(RequisitionOperations.LOGIN, null), new Payload(email, password));
    }

    public record Payload(@NotEmpty @Email String email, @NotEmpty String password) {
    }
}

