package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class DeleteSegmentRequest extends Request<DeleteSegmentRequest.Payload>{
    @NotNull
    @Valid
    public final Payload payload;
    public DeleteSegmentRequest(String token, Long id1, Long id2) {
        super(new Header(RequisitionOperations.DELETAR_SEGMENTO, token));
        payload = new Payload(id1, id2);
    }

    public record Payload(@NotNull Long pdi_inicial, @NotNull Long pdi_final) {}
}
