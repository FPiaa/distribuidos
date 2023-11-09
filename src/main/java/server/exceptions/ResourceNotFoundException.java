package server.exceptions;


public class ResourceNotFoundException extends ServerResponseException {
    public ResourceNotFoundException(final String message) {
        super(404, "Não foi possível encontrar '%s'.".formatted(message));
    }
}
