package protocol.response;

import protocol.commons.EmptyPayload;

public record DeleteUserResponse(EmptyPayload payload) implements Response<EmptyPayload> {
    public DeleteUserResponse() {
        this(new EmptyPayload());
    }
}
