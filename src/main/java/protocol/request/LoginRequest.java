package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class LoginRequest extends Request<LoginRequest.Payload> {

    @NotNull @Valid private final Payload payload;

    public LoginRequest(final String email, final String password) {
        super(new Header(RequisitionOperations.LOGIN, null));
        payload = new Payload(email, password);
    }

    public record Payload(@NotBlank @Email String email, @NotBlank String password) {
    }
}

