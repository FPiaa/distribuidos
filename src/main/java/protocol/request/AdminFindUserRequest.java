package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class AdminFindUserRequest extends Request<AdminFindUserRequest.Payload> {

    @NotNull(message = "payload não pode ser nulo")
    @Valid
    private final Payload payload;

    public AdminFindUserRequest(String token, Integer registro) {
        super(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIO, token));
        payload = new Payload(registro);
    }

    public record Payload(int registro) {
    }
}
