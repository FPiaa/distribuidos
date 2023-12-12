package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.Optional;
import protocol.request.header.Header;

@Getter
public class UpdateSegmentRequest extends Request<UpdateSegmentRequest.Payload>{
    @NotNull
    @Valid
    public final Payload payload;
    public UpdateSegmentRequest(String token, Long id1, Long id2, @Optional String aviso, @Optional Boolean acessivel) {
        super(new Header(RequisitionOperations.ATUALIZAR_SEGMENTO, token));
        payload = new Payload(id1, id2, aviso, acessivel);
    }

    public record Payload(@NotNull Long pdi_inicial, @NotNull Long pdi_final, String aviso, Boolean acessivel) {}
}
