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

    public UpdatePoiRequest(String token, @Optional String name, @Optional Integer x, @Optional Integer y, @Optional String aviso, @Optional Boolean acessivel) {
        super(new Header(RequisitionOperations.ATUALIZAR_PDI, token));
        this.payload = new Payload(name, new Position(x, y), aviso, acessivel);
    }

    public record Payload(String name, @Valid Position position, String aviso, Boolean acessivel) {}
}
