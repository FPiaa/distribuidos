package server.exceptions;


public class MethodNotAllowedException extends ServerResponseException{
    public MethodNotAllowedException(final String metodo) {
        super(405, "%s não reconhecido".formatted(metodo));
    }
}
