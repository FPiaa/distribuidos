package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import protocol.Optional;
import protocol.request.header.Header;

@Getter
public class UpdateUserRequest extends Request<UpdateUserRequest.Payload> {

    @NotNull(message = "payload não pode ser nulo")
    @Valid
    private final Payload payload;

    public UpdateUserRequest(String token, @Optional String email,
                             @Optional String nome, @Optional String senha) {
        super(new Header(RequisitionOperations.ATUALIZAR_USUARIO, token));
        payload = new UpdateUserRequest.Payload(email, nome, senha);
    }

    public record Payload(
        @Email(message = "email deve ser um email válido")
        String email,
        @Size(min = 3, max = 255)
        String nome,
        String senha
        ) {
    }
}
