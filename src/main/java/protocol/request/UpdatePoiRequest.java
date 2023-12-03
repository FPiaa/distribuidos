package protocol.request;

import commons.Position;
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

    public UpdatePoiRequest(String token, Long id, @Optional String name, @Optional Float x, @Optional Float y, @Optional String aviso, @Optional Boolean acessivel) {
        super(new Header(RequisitionOperations.ATUALIZAR_PDI, token));
        if (x == null && y == null) {
            this.payload = new Payload(id, name, null, aviso, acessivel);
        } else {
            this.payload = new Payload(id, name, new Position(x == null ? 0 : x, y == null ? 0 : y), aviso, acessivel);
        }
    }

    public record Payload(@NotNull Long id, String nome, @Valid Position posicao, String aviso, Boolean acessivel) {
    }
}
