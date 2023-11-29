package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;
import commons.Position;

@Getter
public class CreatePoiRequest extends Request<CreatePoiRequest.Payload>{
    @Valid
    @NotNull
    private final Payload payload;
    public CreatePoiRequest(String token, String name, Integer x, Integer y, String aviso, Boolean acessivel) {
        super(new Header(RequisitionOperations.CADASTRAR_PDI, token));
        this.payload = new Payload(name, new Position(x, y), aviso, acessivel);
    }

    public record Payload(String name, Position position, String aviso, Boolean acessivel){}
}
