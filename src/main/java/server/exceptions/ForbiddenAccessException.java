package server.exceptions;


public class ForbiddenAccessException extends ServerResponseException{
    public ForbiddenAccessException(final String message) {
        super(403, "User '%s' does not have enough permission to access this resource.".formatted(message));
    }
}
