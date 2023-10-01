package server.layer.initialLayer;

import json.JsonHelper;
import protocol.request.LogoutRequest;
import protocol.response.LogoutResponse;
import protocol.response.Response;
import server.layer.finishLayer.ProcessLogout;
import server.layer.interfaces.InitialLayer;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateUser;

public class StartLogout implements InitialLayer {
    @Override
    public Response<?> startService(String jsonString) {
        Layer<LogoutRequest, LogoutResponse> validate = new ValidateUser<>();
        validate.buildService(new ProcessLogout());
        var logout = JsonHelper.gson.fromJson(jsonString, LogoutRequest.class);
        return validate.next(logout);
    }
}
