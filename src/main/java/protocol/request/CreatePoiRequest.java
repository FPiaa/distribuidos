package protocol.request;

import commons.Position;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class CreatePoiRequest extends Request<CreatePoiRequest.Payload>{
    @Valid
    @NotNull
    private final Payload payload;
    public CreatePoiRequest(String token, String name, Integer x, Integer y, Integer z, String aviso, Boolean acessivel) {
        super(new Header(RequisitionOperations.CADASTRAR_PDI, token));
        this.payload = new Payload(name, new Position(x, y, z), aviso, acessivel);
    }

    public record Payload(@NotNull @NotBlank String name, @NotNull @Valid Position position, String aviso, @NotNull Boolean acessivel){}
}
