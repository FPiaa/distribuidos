package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.commons.EmptyPayload;
import protocol.request.header.Header;

@Getter
public class FindSegmentsRequest extends Request<EmptyPayload>{
    @NotNull
    @Valid
    public final EmptyPayload payload;
    public FindSegmentsRequest(String token) {
        super(new Header(RequisitionOperations.BUSCAR_SEGMENTOS, token));
        payload = new EmptyPayload();
    }
}
