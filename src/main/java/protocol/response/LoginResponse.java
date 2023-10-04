package protocol.response;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "payload")
public record LoginResponse(Payload payload) implements Response<LoginResponse.Payload> {

    public LoginResponse(final String token) {
        this(new Payload(token));
    }

    public record Payload(String token) {}
}
