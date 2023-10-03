package protocol.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.request.header.Header;

public record LogoutRequest(@NotNull @Valid Header header, Void payload) implements Request<Void> {
    public LogoutRequest(final String userToken) {
        this(new Header(RequisitionOperations.LOGOUT, userToken), null);
    }

}
