package server.exceptions;

import jakarta.validation.constraints.NotNull;
import protocol.response.ErrorResponse;

public abstract class ServerResponseException extends Exception implements IntoResponse {
    private final int errorCode;

    public ServerResponseException(int errorCode, @NotNull final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorResponse intoResponse() {
        return new ErrorResponse(errorCode, getMessage());
    }
}
