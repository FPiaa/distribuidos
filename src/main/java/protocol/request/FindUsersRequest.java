package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record FindUsersRequest(
        @NotNull @Valid Header header,
        Payload payload
) implements Request<FindUsersRequest.Payload> {
    public FindUsersRequest(String token) {
        this(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIOS, token), null);
    }
    public record Payload() {}
}
