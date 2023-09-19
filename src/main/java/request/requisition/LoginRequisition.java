package request.requisition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import request.abstractclasses.Request;
import request.RequisitionOperations;
import request.header.Header;
import request.abstractclasses.RequestPayload;

@EqualsAndHashCode(callSuper = false)
public class LoginRequisition extends Request<LoginRequisition.Payload> {
    public LoginRequisition(String email, String password) {
        super(new Header(RequisitionOperations.LOGIN, null),
                new LoginRequisition.Payload(email, password));
    }

    @EqualsAndHashCode(callSuper = false)
    @Data
    public static class Payload extends RequestPayload {
        @NonNull
        private final String email;
        @NonNull private final String password;
    }
}

