package response;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import response.payload.ResponsePayload;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class LoginResponse extends Response<LoginResponse.Payload> {

    @NonNull
    private final LoginResponse.Payload payload;

    public LoginResponse(String token) {
        this(createPayload(token));
    }

    private static LoginResponse.Payload createPayload(String token) {
        return new LoginResponse.Payload(token);
    }

    @Override
    public LoginResponse.Payload payload() {
        return payload;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @Data
    public static class Payload extends ResponsePayload {
        @NonNull
        private final String token;
    }

}
