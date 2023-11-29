package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.commons.EmptyPayload;
import protocol.request.header.Header;

@Getter
public class FindPoisRequest extends Request<EmptyPayload>{
    @Valid
    @NotNull
    public final EmptyPayload payload;

    public FindPoisRequest(String token) {
        super(new Header(RequisitionOperations.BUSCAR_PDIS, token));
        payload = new EmptyPayload();
    }
}
