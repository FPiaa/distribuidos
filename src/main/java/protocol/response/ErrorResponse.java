package protocol.response;


import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonRootName(value="error")
public record ErrorResponse(@NotNull @Valid Payload error) implements Response<ErrorResponse.Payload> {
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

