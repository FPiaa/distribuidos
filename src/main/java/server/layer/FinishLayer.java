package server.layer;

import request.Request;
import request.payload.RequestPayload;
import response.Response;
import response.payload.ResponsePayload;

public interface FinishLayer<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {
    Res finish(Req requisition);
}
