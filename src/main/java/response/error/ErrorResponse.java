package response.error;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import response.Response;


@EqualsAndHashCode(callSuper = false)
@Data
public class ErrorResponse extends Response<ErrorResponsePayload> {
    @NonNull private ErrorResponsePayload error;

    public ErrorResponse(int code, String message) {
        error = new ErrorResponsePayload(code, message);
    }
    @Override
    public ErrorResponsePayload payload() {
        return error;
    }

    @Override
    public String toJson() {
        var gson = new Gson();
        return gson.toJson(this);
    }
}

