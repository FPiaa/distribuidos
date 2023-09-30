package response;

import org.checkerframework.checker.nullness.qual.NonNull;

public record LogoutResponse(Payload payload) implements Response<LogoutResponse.Payload>{

    public LogoutResponse() {
        this(new Payload("desconectado"));
    }

    public record Payload(@NonNull String message) {
    }
}
