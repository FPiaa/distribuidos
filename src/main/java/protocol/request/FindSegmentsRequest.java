package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.Optional;
import protocol.commons.EmptyPayload;
import protocol.request.header.Header;

@Getter
public class FindSegmentsRequest extends Request<EmptyPayload>{
    @NotNull
    @Valid
    public final EmptyPayload payload;
    public FindSegmentsRequest(String token, Long id1, Long id2, @Optional String aviso, Boolean acessivel) {
        super(new Header(RequisitionOperations.BUSCAR_SEGMENTOS, token));
        payload = new EmptyPayload();
    }
}
