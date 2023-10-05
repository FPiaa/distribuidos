package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class LoginRequest extends Request<LoginRequest.Payload> {

    @NotNull(message = "payload não pode ser nulo")
    @Valid private final Payload payload;

    public LoginRequest(final String email, final String password) {
        super(new Header(RequisitionOperations.LOGIN, null));
        payload = new Payload(email, password);
    }

    public record Payload(
            @NotBlank(message = "email não pode estar vazio")
                    @Email
            String email,
            @NotBlank(message = "password não pode estar vazio") String password) {
    }
}

