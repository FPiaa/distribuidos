package server.exceptions;

import org.checkerframework.checker.nullness.qual.NonNull;

public class BadRequestException extends ServerResponseException {

    public BadRequestException(@NonNull final String message) {
        super(400, message);
    }
    public BadRequestException(int code, @NonNull final String message ) {
        super(code, message);
    }
}
