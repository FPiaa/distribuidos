package server.layer.initialLayer;

import protocol.request.AdminCreateUserRequest;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

public class StartAdminCreateUser extends StartTemplate<AdminCreateUserRequest>{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        return null;
    }
}
