package server.exceptions;

public class InvalidTokenException extends ServerResponseException {

    public InvalidTokenException() {
        super(42, "O token enviado não é válido");
    }
}
