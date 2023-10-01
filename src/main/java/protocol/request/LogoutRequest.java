package protocol.request;

import json.annotation.JsonOptional;
import org.checkerframework.checker.nullness.qual.NonNull;
import protocol.request.header.Header;

public record LogoutRequest(@NonNull Header header,
                            @JsonOptional Payload payload) implements Request<LogoutRequest.Payload> {
    public LogoutRequest(@NonNull String userToken) {
        this(new Header(RequisitionOperations.LOGOUT, userToken), null);
    }

    public static class Payload {
    }
}