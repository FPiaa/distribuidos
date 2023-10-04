package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import protocol.request.header.Header;

public record FindUserRequest(
        @NotNull @Valid Header header,
        @NotNull @Valid Payload payload)
        implements Request<FindUserRequest.Payload> {

    public FindUserRequest(String token, Integer registro) {
        this(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIO, token), new Payload(registro));
    }
    public record Payload(@Positive int registro) {}
}
