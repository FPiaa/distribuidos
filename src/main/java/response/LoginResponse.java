package response;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import response.payload.LoginResponsePayload;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class LoginResponse extends Response<LoginResponsePayload>{

    @NonNull private final LoginResponsePayload payload;
    public LoginResponse(String token) {
        this(createPayload(token));
    }

    private static LoginResponsePayload createPayload(String token) {
        return new LoginResponsePayload(token);
    }

    @Override
    public LoginResponsePayload payload() {
        return payload;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
