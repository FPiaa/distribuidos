package server.layer.initialLayer;

import com.google.gson.JsonSyntaxException;
import json.JsonHelper;
import protocol.request.LogoutRequest;
import protocol.response.LogoutResponse;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessLogout;
import server.layer.interfaces.InitialLayer;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateUser;

public class StartLogout implements InitialLayer {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException, JsonSyntaxException {
        Layer<LogoutRequest, LogoutResponse> validate = new ValidateUser<>();
        validate.buildService(new ProcessLogout());
        var logout = JsonHelper.fromJson(jsonString, LogoutRequest.class);
        return validate.next(logout);
    }
}
