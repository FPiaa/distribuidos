package request.requisition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.checkerframework.checker.nullness.qual.NonNull;
import request.RequisitionOperations;
import request.abstractclasses.Request;
import request.abstractclasses.RequestPayload;
import request.header.Header;

import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
public class LoginRequisition extends Request<LoginRequisition.Payload> {
    public LoginRequisition(String email, String password) {
        super(new Header(RequisitionOperations.LOGIN, Optional.empty()),
                new LoginRequisition.Payload(email, password));
    }

    @EqualsAndHashCode(callSuper = false)
    @Data
    public static class Payload extends RequestPayload {
        @NonNull
        private final String email;
        @lombok.NonNull
        private final String password;
    }
}

