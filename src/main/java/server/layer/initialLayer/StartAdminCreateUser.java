package server.layer.initialLayer;

import protocol.request.AdminCreateUserRequest;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

public class StartAdminCreateUser extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, AdminCreateUserRequest.class);
        return null;
    }
}
