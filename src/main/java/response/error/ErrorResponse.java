package response.error;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import response.Response;
import response.payload.ResponsePayload;


@EqualsAndHashCode(callSuper = false)
@Data
public class ErrorResponse extends Response<ErrorResponse.Payload> {
    @NonNull
    private final ErrorResponse.Payload error;

    public ErrorResponse(int code, String message) {
        error = new ErrorResponse.Payload(code, message);
    }

    @Override
    public ErrorResponse.Payload payload() {
        return error;
    }

    @Override
    public String toJson() {
        var gson = new Gson();
        return gson.toJson(this);
    }

    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @Data
    public static class Payload extends ResponsePayload {
        private int code;
        @NonNull
        private String message;
    }
}

