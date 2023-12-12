package server.layer.initialLayer;

import protocol.request.LoginRequest;
import protocol.response.LoginResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.exceptions.ServerResponseException;

public class StartLogin extends StartTemplate {

    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        LoginRequest loginRequest = buildRequest(jsonString, LoginRequest.class);

        String token = UserController.getInstance().login(loginRequest.getPayload());
        return new LoginResponse(token);
    }
}
