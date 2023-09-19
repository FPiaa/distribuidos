package server.layer;

import request.abstractclasses.Request;
import request.abstractclasses.RequestPayload;
import response.Response;
import response.payload.ResponsePayload;

public interface InitialLayer<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {
    Layer<Req, Res> startService(String jsonString);
}
