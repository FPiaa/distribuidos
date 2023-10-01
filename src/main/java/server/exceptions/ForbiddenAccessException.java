package server.exceptions;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ForbiddenAccessException extends ServerResponseException{
    public ForbiddenAccessException(@NonNull String message) {
        super(403, "User '%s' does not have enough permission to access this resource.".formatted(message));
    }
}
