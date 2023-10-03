package protocol.response;

import jakarta.validation.constraints.NotEmpty;

public record LoginResponse(@NotEmpty Payload payload) implements Response<LoginResponse.Payload> {


    public LoginResponse(final String token) {
        this(new Payload(token));
    }


    public record Payload(@NotEmpty String token) {
    }

}
