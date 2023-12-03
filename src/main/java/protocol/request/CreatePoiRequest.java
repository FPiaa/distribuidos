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
    public CreatePoiRequest(String token, String nome, Float x, Float y, String aviso, Boolean acessivel) {
        super(new Header(RequisitionOperations.CADASTRAR_PDI, token));
        this.payload = new Payload(nome, new Position(x, y), aviso, acessivel);
    }

    public record Payload(@NotNull @NotBlank String nome, @NotNull @Valid Position posicao, String aviso, @NotNull Boolean acessivel){}
}
