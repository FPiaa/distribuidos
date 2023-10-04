package server.layer.initialLayer;

import protocol.request.LoginRequest;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessLogin;

public class StartLogin extends StartTemplate<LoginRequest> {

    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        LoginRequest loginRequest = buildRequest(jsonString, LoginRequest.class);
        return new ProcessLogin().finish(loginRequest);
    }
}
