package server.layer;

import request.abstractclasses.Request;
import request.abstractclasses.RequestPayload;
import response.Response;
import response.payload.ResponsePayload;

public interface Layer<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {
    boolean check();
    Res next(Req requisition);

    Layer<Req, Res> addLayer(Layer<Req, Res> newLayer);

    Layer<Req, Res> buildService(FinishLayer<Req, Res> consumer) ;

}
