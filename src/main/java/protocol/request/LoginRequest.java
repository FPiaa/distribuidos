package protocol.request;

import protocol.request.header.Header;

public record LoginRequest(Header header,
                           Payload payload) implements Request<LoginRequest.Payload> {

    public LoginRequest(final String email, final String password) {
        this(new Header(RequisitionOperations.LOGIN, null), new Payload(email, password));
    }

    public record Payload(String email, String password) {
    }
}

