package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class AdminCreateUserRequest extends Request<AdminCreateUserRequest.Payload> {

    @NotNull(message = "payload não pode ser nulo")
    @Valid
    private final Payload payload;

    public AdminCreateUserRequest(final String token, final String nome, final String email,
                                  final String senha, final Boolean tipo) {
        super(new Header(RequisitionOperations.ADMIN_CADASTRAR_USUARIO, token));
        payload = new Payload(nome, email, senha, tipo);
    }

    public record Payload(
            @NotBlank(message = "nome não pode estar vazio")
            String nome,
            @NotBlank(message = "email não pode estar vazio")
            String email,
            @NotBlank(message = "senha não pode estar vazio")
            String senha,
            @NotNull(message = "tipo não pode ser nulo")
            Boolean tipo
    ) {
    }
}
