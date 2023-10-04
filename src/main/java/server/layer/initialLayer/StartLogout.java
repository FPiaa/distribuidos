package server.layer.initialLayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import helper.json.JsonHelper;
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
    public Response<?> startService(String jsonString) throws ServerResponseException {
        Layer<LogoutRequest, LogoutResponse> validate = new ValidateUser<>();
        validate.buildService(new ProcessLogout());
        LogoutRequest logout = null;
        try {
            logout = JsonHelper.fromJson(jsonString, LogoutRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return validate.next(logout);
    }
}
