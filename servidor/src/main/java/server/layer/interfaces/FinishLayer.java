package server.layer.interfaces;

import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

public interface FinishLayer<Req extends Request<?>, Res extends Response<?>> {

    Res finish(Req request) throws ServerResponseException;
}
