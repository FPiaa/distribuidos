package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.Optional;
import protocol.request.header.Header;

@Getter
public class UpdatePoiRequest extends Request<UpdatePoiRequest.Payload> {
    @Valid
    @NotNull
    private final Payload payload;

    public UpdatePoiRequest(String token, Long id, @Optional String name, @Optional Double x, @Optional Double y, @Optional String aviso, @Optional Boolean acessivel) {
        super(new Header(RequisitionOperations.ATUALIZAR_PDI, token));
            this.payload = new Payload(id, name, aviso, acessivel);
    }

    public record Payload(@NotNull Long id, String nome, String aviso, Boolean acessivel) {
    }
}
