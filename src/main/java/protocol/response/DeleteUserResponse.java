package protocol.response;

public record DeleteUserResponse(Payload payload) implements Response<DeleteUserResponse.Payload> {
    public DeleteUserResponse() {
        this(new Payload("Usuario deletado"));
    }

    public record Payload(String mensagem){}
}
