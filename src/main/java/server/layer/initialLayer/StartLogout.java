package server.layer.initialLayer;

import protocol.request.LogoutRequest;
import protocol.response.LogoutResponse;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessLogout;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateUser;

public class StartLogout extends StartTemplate<LogoutRequest>{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        Layer<LogoutRequest, LogoutResponse> validate = new ValidateUser<>();
        validate.buildService(new ProcessLogout());
        return validate.next(buildRequest(jsonString, LogoutRequest.class));
    }
}
