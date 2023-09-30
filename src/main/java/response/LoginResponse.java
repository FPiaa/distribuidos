package response;

import org.checkerframework.checker.nullness.qual.NonNull;

public record LoginResponse(Payload payload) implements Response<LoginResponse.Payload> {


    public LoginResponse(@NonNull String token) {
        this(new Payload(token));
    }


    public record Payload(@NonNull String token) {
    }

}
