package server.layer.initialLayer;

import json.JsonHelper;
import protocol.request.LoginRequest;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessLogin;
import server.layer.interfaces.InitialLayer;

public class StartLogin implements InitialLayer {

    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var loginRequest = JsonHelper.fromJson(jsonString, LoginRequest.class);
        return new ProcessLogin().finish(loginRequest);
    }
}
