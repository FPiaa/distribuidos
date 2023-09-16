package request.requisition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import request.Request;
import request.RequisitionOperations;
import request.header.Header;
import request.payload.LoginPayload;

@EqualsAndHashCode(callSuper = false)
@Data
public class LoginRequisition extends Request<LoginPayload> {
    @NonNull
    private Header header;
    private LoginPayload payload;
    public LoginRequisition(String email, String password) {
        header = new Header(RequisitionOperations.LOGIN, null) ;
        payload = new LoginPayload(email, password) ;
    }

    @Override
    public Header header() {
        return header;
    }

    @Override
    public LoginPayload payload() {
        return payload;
    }
}

