package server.exceptions;


public class ForbiddenAccessException extends ServerResponseException {

    public ForbiddenAccessException() {
        super(403, "Usuário não tem permissão para acessar o recurso");
    }

    public ForbiddenAccessException(final long userId) {
        super(403, "Usuário '%d' não tem permissão para acessar o recurso.".formatted(userId));
    }
}
