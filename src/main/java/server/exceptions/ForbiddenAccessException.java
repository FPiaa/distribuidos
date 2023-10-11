package server.exceptions;


public class ForbiddenAccessException extends ServerResponseException {

    public ForbiddenAccessException() {
        super(403, "User does not have enough permission to access this resource.");
    }

    public ForbiddenAccessException(final long userId) {
        super(403, "User '%d' does not have enough permission to access this resource.".formatted(userId));
    }
}
