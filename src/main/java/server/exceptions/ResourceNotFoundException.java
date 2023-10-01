package server.exceptions;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ResourceNotFoundException extends ServerResponseException{
    public ResourceNotFoundException(@NonNull String message) {
        super(404, "Unable to find '%s'.".formatted(message));
    }
}
