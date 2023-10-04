package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record LoginRequest(
        @NotNull @Valid Header header,
        @NotNull @Valid Payload payload
) implements Request<LoginRequest.Payload> {

    public LoginRequest(final String email, final String password) {
        this(new Header(RequisitionOperations.LOGIN, null), new Payload(email, password));
    }

    public record Payload(@NotBlank @Email String email, @NotBlank String password) {
    }
}

