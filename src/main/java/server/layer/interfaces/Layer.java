package server.layer.interfaces;

import protocol.request.Request;
import protocol.response.Response;

@SuppressWarnings("unused")
public interface Layer<Req extends Request<?>, Res extends Response<?>> {
    boolean check(Req request);


    Response<?> next(Req request);

    Layer<Req, Res> addLayer(Layer<Req, Res> newLayer);

    void buildService(FinishLayer<Req, Res> consumer);

}