package protocol.request;

import protocol.request.header.Header;

public record LogoutRequest(Header header, Void payload) implements Request<Void> {
    public LogoutRequest(final String userToken) {
        this(new Header(RequisitionOperations.LOGOUT, userToken), null);
    }

}
