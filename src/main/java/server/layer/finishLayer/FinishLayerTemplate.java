package server.layer.finishLayer;

import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.FinishLayer;
import server.layer.interfaces.Layer;

public abstract class FinishLayerTemplate<Req extends Request<?>, Res extends Response<?>> implements FinishLayer<Req, Res> {

    @Override
    public void check(Req request) throws ServerResponseException {
        throw new RuntimeException("Calling check in a finish layer. This should never happen.");
    }

    @Override
    public Res next(Req request) throws ServerResponseException {
        throw new RuntimeException("Calling next in a finish layer. This should never happen.");
    }

    @Override
    public Layer<Req, Res> addLayer(Layer<Req, Res> newLayer) {
        throw new RuntimeException("Calling addLayer in a finish layer. This should never happen.");
    }

    @Override
    public void buildService(FinishLayer<Req, Res> consumer) {
        throw new RuntimeException("Calling buildService in a finish layer. This should never happen.");
    }
}
