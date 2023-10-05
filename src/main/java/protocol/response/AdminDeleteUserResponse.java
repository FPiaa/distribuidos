package protocol.response;

import protocol.commons.EmptyPayload;

public record AdminDeleteUserResponse(EmptyPayload payload) implements Response<EmptyPayload> {
    public AdminDeleteUserResponse() {
        this(new EmptyPayload());
    }
}
