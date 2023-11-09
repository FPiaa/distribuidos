package server.layer.middleware;

import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.FinishLayer;
import server.layer.interfaces.Layer;

public abstract class LayerTemplate<Req extends Request<?>, Res extends Response<?>> implements Layer<Req, Res> {
    protected Layer<Req, Res> next = null;
    private FinishLayer<Req, Res> finish = null;

    @Override
    public Res next(Req request) throws ServerResponseException {
        check(request);
        if (finish != null) {
            return finish.finish(request);
        }
        return next.next(request);
    }

    @Override
    public Layer<Req, Res> addLayer(Layer<Req, Res> newLayer) {
        if (next == null) {
            next = newLayer;
            return this;
        } else {
            next.addLayer(newLayer);
        }
        return this;
    }

    @Override
    public Layer<Req, Res> buildService(FinishLayer<Req, Res> consumer) {
        if (next != null) {
            next.buildService(consumer);
        } else {
            finish = consumer;
        }
        return this;
    }
}
