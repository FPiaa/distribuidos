package protocol.request;

import jakarta.validation.constraints.Positive;
import protocol.request.header.Header;

public record FindUserRequest(Header header, Payload payload) implements Request<FindUserRequest.Payload> {

    public FindUserRequest(String token, Integer registro) {
        this(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIO, token), new Payload(registro));
    }
    public record Payload(@Positive int registro) {}
}
