package server.exceptions;

import org.checkerframework.checker.nullness.qual.NonNull;

public class BadRequestException extends ServerResponseException {
    public BadRequestException() {
        super(400, "Obligatory fields are missing.");
    }

    public BadRequestException(@NonNull final String message) {
        super(400, "Obligatory fields '%s' are missing.".formatted(message));
    }
}
