package request.requisition;

import request.Request;
import request.RequisitionOperations;
import request.header.Header;
import request.payload.LoginPayload;

public record LoginRequisition(Header header, LoginPayload payload) implements Request<LoginPayload> {
    public LoginRequisition(String email, String password) {
       this(createHeader(), createPayload(email, password));
    }

    private static Header createHeader() {
       return new Header(RequisitionOperations.LOGIN.name(), null) ;
    }

    private static LoginPayload createPayload(String email, String password) {
       return new LoginPayload(email, password) ;
    }

}

