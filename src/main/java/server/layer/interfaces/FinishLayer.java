package server.layer.interfaces;

import request.Request;
import response.Response;

public interface FinishLayer<Req extends Request<?>, Res extends Response<?>> extends Layer<Req, Res> {

    Res finish(Req request);
}
