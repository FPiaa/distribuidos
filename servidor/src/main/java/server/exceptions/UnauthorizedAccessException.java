package server.exceptions;

public class UnauthorizedAccessException extends ServerResponseException {
    public UnauthorizedAccessException() {
        super(401, "Não foi possível autenticar o usuário");
    }
}
