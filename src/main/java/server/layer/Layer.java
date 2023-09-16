package server.layer;

import request.Request;
import request.payload.RequestPayload;
import response.Response;
import response.payload.ResponsePayload;

public interface Layer<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {
    boolean check();
    Res next(Req requisition);
}
