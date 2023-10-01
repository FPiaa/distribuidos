package server.exceptions;

public class UnauthorizedAccessException extends ServerResponseException{
    public UnauthorizedAccessException() {
        super(401, "Unable to authenticate user, invalid token.");
    }
}
