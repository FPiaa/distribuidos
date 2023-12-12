package protocol.request;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import protocol.request.header.Header;

@Getter
public class DeleteUserRequest extends Request<DeleteUserRequest.Payload> {
    @NotNull
    @Valid
    private final DeleteUserRequest.Payload payload;

    public DeleteUserRequest(String token, String email, String senha) {
        super(new Header(RequisitionOperations.DELETAR_USUARIO, token));
        payload = new DeleteUserRequest.Payload(email, senha);
    }

    public record Payload(
            @NotBlank(message = "Email não pode estar vazio")
            @Email(message = "o campo email deve ser um email válido")
            String email,
            @NotBlank(message = "O campo password não pode estar vazio")
                    @SerializedName(value = "senha")
            String password

    ) {
    }
}
