package protocol.response;

public record LogoutResponse(Payload payload) implements Response<LogoutResponse.Payload> {

    public LogoutResponse() {
        this(new Payload("desconectado"));
    }
    public record Payload(String mensagem) { }
}