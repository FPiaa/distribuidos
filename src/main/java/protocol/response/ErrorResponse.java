package protocol.response;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ErrorResponse(@NotNull Payload error) implements Response<ErrorResponse.Payload> {
    public ErrorResponse(@Positive int code, @NotEmpty String message) {
        this(new ErrorResponse.Payload(code, message));
    }

    @Override
    public Payload payload() {
        return error;
    }


    public record Payload(int code, @NotNull String message) {
    }
}

