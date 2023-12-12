package protocol.request;

import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class FindRouteRequest extends Request<FindRouteRequest.Payload>{
    private final Payload payload;

    public FindRouteRequest(String token, Long pdi_inicial, Long pdi_final) {
        super(new Header(RequisitionOperations.BUSCAR_ROTA, token));
        payload = new Payload(pdi_inicial, pdi_final);

    }
    public record Payload(Long pdi_inicial, Long pdi_final) {}
}
