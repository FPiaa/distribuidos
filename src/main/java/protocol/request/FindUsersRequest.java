package protocol.request;

import protocol.request.header.Header;

public record FindUsersRequest(Header header, Void payload) implements Request<Void> {
    public FindUsersRequest(String token) {
        this(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIOS, token), null);
    }
}
