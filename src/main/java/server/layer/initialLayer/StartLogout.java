package server.layer.initialLayer;

import protocol.request.LogoutRequest;
import protocol.response.LogoutResponse;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateToken;

public class StartLogout extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        Layer<LogoutRequest, LogoutResponse> validate = new ValidateToken<>();
        validate.buildService((request -> new LogoutResponse()));
        return validate.next(buildRequest(jsonString, LogoutRequest.class));
    }
}
