package protocol.response;

import protocol.request.EmptyPayload;

public record AdminDeleteUserResponse(EmptyPayload payload) implements Response<EmptyPayload> {
    public AdminDeleteUserResponse() {
        this(new EmptyPayload());
    }
}
