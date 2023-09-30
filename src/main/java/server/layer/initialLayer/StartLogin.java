package server.layer.initialLayer;

import json.JsonHelper;
import request.LoginRequest;
import response.Response;
import server.layer.interfaces.InitialLayer;
import server.layer.finishLayer.ProcessLogin;

public class StartLogin implements InitialLayer {

    @Override
    public Response<?> startService(String jsonString) {
        var loginRequest = JsonHelper.gson.fromJson(jsonString, LoginRequest.class);
        return new ProcessLogin().finish(loginRequest);
    }
}
