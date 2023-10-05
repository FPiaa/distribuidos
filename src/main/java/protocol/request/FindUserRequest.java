package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class FindUserRequest extends Request<FindUserRequest.Payload> {

    @NotNull @Valid private final Payload payload;

    public FindUserRequest(String token, Integer registro) {
        super(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIO, token));
        payload = new Payload(registro);
    }

    public record Payload(@Positive int registro) {
    }
}
