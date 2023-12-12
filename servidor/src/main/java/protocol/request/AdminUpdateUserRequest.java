package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import protocol.Optional;
import protocol.request.header.Header;

@Getter
public class AdminUpdateUserRequest extends Request<AdminUpdateUserRequest.Payload> {
    @NotNull
    @Valid
    private final Payload payload;

    public AdminUpdateUserRequest(String token, Long registro, @Optional String email,
                                  @Optional String nome, @Optional String senha,
                                  @Optional Boolean tipo) {
        super(new Header(RequisitionOperations.ADMIN_ATUALIZAR_USUARIO, token));
        payload = new Payload(registro, email, nome, senha, tipo);
    }

    public record Payload(
            @Positive(message = "registro deve ser positivo")
            @NotNull
            Long registro,
            @Email(message = "email deve ser um email válido")
            String email,
            @Size(min = 3, max = 255)
            String nome,
            String senha,
            Boolean tipo) {
    }
}
