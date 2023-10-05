package protocol.request;

import protocol.request.header.Header;

public class AdminFindUsersRequest extends Request<EmptyPayload> {
    public AdminFindUsersRequest(final String token) {
        super(new Header(RequisitionOperations.ADMIN_BUSCAR_USUARIOS, token));
    }
}
