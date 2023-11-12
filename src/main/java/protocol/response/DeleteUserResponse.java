package protocol.response;

import jakarta.validation.constraints.NotBlank;

public record DeleteUserResponse(Payload payload) implements Response<DeleteUserResponse.Payload> {
    public DeleteUserResponse() {
        this(new Payload("Usuario deletado"));
    }

    public record Payload(@NotBlank String mensagem){}
}
