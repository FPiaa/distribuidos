package response;

import org.checkerframework.checker.nullness.qual.NonNull;


public record ErrorResponse(@NonNull Payload error) implements Response<ErrorResponse.Payload> {
    public ErrorResponse(int code, String message) {
        this(new ErrorResponse.Payload(code, message));
    }

    @Override
    public Payload payload() {
        return error;
    }


    public record Payload(int code, @NonNull String message) {
    }
}

