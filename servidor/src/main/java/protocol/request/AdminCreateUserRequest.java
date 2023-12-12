package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
            @Size(min = 3, max = 255, message = "nome deve conter entre 3 e 255 caracteres")
            String nome,
            @NotBlank(message = "email não pode estar vazio")
            @Email
            String email,
            @NotBlank(message = "senha não pode estar vazio")
            String senha,
            @NotNull(message = "tipo não pode ser nulo")
            Boolean tipo
    ) {
    }
}
