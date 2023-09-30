package request.requisition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.checkerframework.checker.nullness.qual.NonNull;
import request.RequisitionOperations;
import request.abstractclasses.Request;
import request.abstractclasses.RequestPayload;
import request.header.Header;

@EqualsAndHashCode(callSuper = false)
public class LoginRequest extends Request<LoginRequest.Payload> {
    public LoginRequest(String email, String password) {
        super(new Header(RequisitionOperations.LOGIN, null),
                new LoginRequest.Payload(email, password));
    }

    @EqualsAndHashCode(callSuper = false)
    @Data
    public static class Payload extends RequestPayload {
        @NonNull
        private final String email;
        @NonNull
        private final String password;
    }
}

