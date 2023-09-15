package response;

import com.google.gson.Gson;
import lombok.NonNull;
import response.error.ErrorResponse;
import response.payload.LoginResponsePayload;

public record LoginResponse(@NonNull LoginResponsePayload payload) implements Response<LoginResponsePayload>{
    public LoginResponse(String token) {
        this(createPayload(token));
    }

    private static LoginResponsePayload createPayload(String token) {
        return new LoginResponsePayload(token);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public ErrorResponse error() {
        return null;
    }
}
