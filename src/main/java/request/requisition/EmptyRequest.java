package request.requisition;

import lombok.ToString;
import request.abstractclasses.Request;
import request.abstractclasses.RequestPayload;
import request.header.Header;

@ToString
public class EmptyRequest extends Request<EmptyRequest.Payload> {

    public EmptyRequest(Header header, EmptyRequest.Payload payload) {
        super(header, payload);
    }

    public static class Payload extends RequestPayload {

    }
}
