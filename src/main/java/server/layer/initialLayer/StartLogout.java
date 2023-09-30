package server.layer.initialLayer;

import json.JsonHelper;
import request.requisition.LogoutRequest;
import response.LogoutResponse;
import response.Response;
import server.interfaces.InitialLayer;
import server.interfaces.Layer;
import server.layer.finishLayer.ProcessLogout;
import server.layer.middleware.ValidateUser;

public class StartLogout implements InitialLayer {
    @Override
    public Response<?> startService(String jsonString) {
        Layer<LogoutRequest, LogoutResponse>  validate = new ValidateUser<>();
        validate.buildService(new ProcessLogout());
        var logout = JsonHelper.gson.fromJson(jsonString, LogoutRequest.class);
        return validate.next(logout);
    }
}
