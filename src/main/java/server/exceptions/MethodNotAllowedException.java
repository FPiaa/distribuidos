package server.exceptions;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MethodNotAllowedException extends ServerResponseException{
    public MethodNotAllowedException(@NonNull final String metodo) {
        super(405, "%s não reconhecido".formatted(metodo));
    }
}
