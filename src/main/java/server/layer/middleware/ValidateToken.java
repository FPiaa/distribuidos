package server.layer.middleware;

import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

public class ValidateToken<Req extends Request<?>, Res extends Response<?>> extends LayerTemplate<Req, Res> {
    @Override
    public void check(Req request) throws ServerResponseException {
        addLayer(new ValidateTokenField<Req, Res>())
            .addLayer(new ValidateUserExists<>());
    }
}
