package protocol.response;


import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ErrorResponse(@NotNull @Valid Payload error) implements Response<ErrorResponse.Payload> {
    public ErrorResponse(int code, String message) {
        this(new ErrorResponse.Payload(code, message));
    }

    @Override
    public Payload payload() {
        return error;
    }


    public record Payload(@Positive int code, @NotBlank @SerializedName(value = "mensagem") String message) {
    }
}

