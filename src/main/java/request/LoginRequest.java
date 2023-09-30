package request;

import org.checkerframework.checker.nullness.qual.NonNull;
import request.header.Header;

public record LoginRequest(@NonNull Header header,
                           LoginRequest.@NonNull Payload payload) implements Request<LoginRequest.Payload> {

    public LoginRequest(@NonNull String email, @NonNull String password) {
        this(new Header(RequisitionOperations.LOGIN, null), new Payload(email, password));
    }

    public record Payload(@NonNull String email, @NonNull String password) {
    }
}

