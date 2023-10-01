package server.layer.interfaces;

import protocol.request.Request;
import protocol.response.Response;

public interface FinishLayer<Req extends Request<?>, Res extends Response<?>> extends Layer<Req, Res> {

    Res finish(Req request);
}
