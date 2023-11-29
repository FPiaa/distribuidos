package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class DeletePoiRequest extends Request<DeletePoiRequest.Payload> {
    @Valid
    @NotNull
    private final Payload payload;

    public DeletePoiRequest(String token, Long id) {
        super(new Header(RequisitionOperations.DELETAR_PDI, token));
        this.payload = new Payload(id);
    }

    public record Payload(Long id) {}
}
