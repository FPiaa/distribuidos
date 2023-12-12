package server.layer.initialLayer;

import protocol.response.LogoutResponse;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

public class StartLogout extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        return new LogoutResponse();
    }
}
