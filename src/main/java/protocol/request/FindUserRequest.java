package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.commons.EmptyPayload;
import protocol.request.header.Header;

@Getter
public class FindUserRequest extends Request<EmptyPayload> {
    @NotNull(message = "payload não pode ser nulo")
    @Valid
    private final EmptyPayload payload;
    public FindUserRequest(String token) {
        super(new Header(RequisitionOperations.BUSCAR_USUARIO, token));
        payload = new EmptyPayload();
    }

}
