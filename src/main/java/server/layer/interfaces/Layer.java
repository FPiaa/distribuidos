package server.layer.interfaces;

import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

@SuppressWarnings("unused")
public interface Layer<Req extends Request<?>, Res extends Response<?>> {
    void check(Req request) throws ServerResponseException;

    Res next(Req request) throws ServerResponseException;

    Layer<Req, Res> addLayer(Layer<Req, Res> newLayer);

    Layer<Req, Res> buildService(FinishLayer<Req, Res> consumer);

}
