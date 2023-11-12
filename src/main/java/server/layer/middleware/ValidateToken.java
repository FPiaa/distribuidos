package server.layer.middleware;

import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

public class ValidateToken<Req extends Request<?>, Res extends Response<?>> extends LayerTemplate<Req, Res> {
    public ValidateToken() {
        addLayer(new ValidateTokenField<Req, Res>())
                .addLayer(new ValidateUserExists<>());
    }
    @Override
    public void check(Req request) throws ServerResponseException {
        return;
    }
}
