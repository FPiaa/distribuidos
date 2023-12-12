package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class CreateUserRequest extends Request<CreateUserRequest.Payload>{
    @NotNull(message = "payload não pode ser nulo")
    @Valid
    private final CreateUserRequest.Payload payload;

    public CreateUserRequest(final String nome, final String email,
                             final String senha) {
        super(new Header(RequisitionOperations.CADASTRAR_USUARIO, null));
        payload = new CreateUserRequest.Payload(nome, email, senha, false);
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
            @AssertFalse(message = "tipo deve ser falso")
            Boolean tipo
    ) {
    }

}
