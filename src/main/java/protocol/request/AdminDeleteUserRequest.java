package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class AdminDeleteUserRequest extends Request<AdminDeleteUserRequest.Payload> {
    @NotNull
    @Valid
    private final Payload payload;

    public AdminDeleteUserRequest(String token, Long registro) {
        super(new Header(RequisitionOperations.ADMIN_DELETAR_USUARIO, token));
        payload = new Payload(registro);
    }

    public record Payload(
            @Positive
            @NotNull
            Long registro
    ) {
    }
}
