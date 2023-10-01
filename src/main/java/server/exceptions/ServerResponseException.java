package server.exceptions;

import org.checkerframework.checker.nullness.qual.NonNull;
import protocol.response.ErrorResponse;

public abstract class ServerResponseException extends Exception implements IntoResponse {
    private final int errorCode;

    public ServerResponseException(int errorCode, @NonNull final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorResponse intoResponse() {
        return new ErrorResponse(errorCode, getMessage());
    }
}
