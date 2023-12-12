package protocol.request;

import jakarta.validation.Valid;
import lombok.Getter;
import protocol.commons.EmptyPayload;
import protocol.request.header.Header;

@Getter
public class FindUserRequest extends Request<EmptyPayload> {
    @Valid
    private final EmptyPayload payload;
    public FindUserRequest(String token) {
        super(new Header(RequisitionOperations.BUSCAR_USUARIO, token));
        payload = new EmptyPayload();
    }

}
