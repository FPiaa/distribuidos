package protocol.request;

import protocol.request.header.Header;

public class FindUsersRequest extends Request<EmptyPayload> {
    public FindUsersRequest(final String token) {
        super(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIOS, token));
    }
}
