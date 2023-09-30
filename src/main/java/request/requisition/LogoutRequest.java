package request.requisition;

import org.checkerframework.checker.nullness.qual.NonNull;
import request.RequisitionOperations;
import request.abstractclasses.Request;
import request.abstractclasses.RequestPayload;
import request.header.Header;

public class LogoutRequest extends Request<LogoutRequest.Payload> {
    public LogoutRequest(@NonNull String userToken) {
       super(new Header(RequisitionOperations.LOGOUT, userToken), null);
    }

    public static class Payload extends RequestPayload{}
}
