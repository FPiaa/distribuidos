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

    public UpdatePoiRequest(String token, Long id, @Optional String name, @Optional Integer x, @Optional Integer y, @Optional Integer z, @Optional String aviso, @Optional Boolean acessivel) {
        super(new Header(RequisitionOperations.ATUALIZAR_PDI, token));
        if (x == null && y == null && z == null) {
            this.payload = new Payload(id, name, null, aviso, acessivel);
        } else {
            this.payload = new Payload(id, name, new Position(x == null ? 0 : x, y == null ? 0 : y, z == null ? 0 : z), aviso, acessivel);
        }
    }

    public record Payload(@NotNull Long id, String nome, @Valid Position posicao, String aviso, Boolean acessivel) {
    }
}
