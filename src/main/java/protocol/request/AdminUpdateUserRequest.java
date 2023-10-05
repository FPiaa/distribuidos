package protocol.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import protocol.Optional;
import protocol.request.header.Header;

@Getter
public class AdminUpdateUserRequest extends Request<AdminUpdateUserRequest.Payload> {
    private final Payload payload;

    public AdminUpdateUserRequest(String token, Integer registro, @Optional String email,
                                  @Optional String nome, @Optional String senha,
                                  @Optional Boolean tipo) {
        super(new Header(RequisitionOperations.ADMIN_ATUALIZAR_USUARIO, token));
        payload = new Payload(registro, email, nome, senha, tipo);
    }

    public record Payload(
            @Positive
            Integer registro,
            @Email
            String email,
            @Size(min = 3, max = 255)
            String nome,
            String senha,
            Boolean tipo) {
    }
}
