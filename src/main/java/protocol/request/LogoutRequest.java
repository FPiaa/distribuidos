package protocol.request;

import protocol.request.header.Header;

public class LogoutRequest extends Request<EmptyPayload> {
    public LogoutRequest(final String userToken) {
        super(new Header(RequisitionOperations.LOGOUT, userToken));
    }
}
