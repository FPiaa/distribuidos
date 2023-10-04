package protocol.response;


import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="error")
public record ErrorResponse(Payload error) implements Response<ErrorResponse.Payload> {
    public ErrorResponse(int code, String message) {
        this(new ErrorResponse.Payload(code, message));
    }

    @Override
    public Payload payload() {
        return error;
    }


    public record Payload(int code, String message) {
    }
}

