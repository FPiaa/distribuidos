package server.layer.initialLayer;

import protocol.request.AdminCreateUserRequest;
import protocol.response.AdminCreateUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.entity.UserEntity;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartAdminCreateUser extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, AdminCreateUserRequest.class);
        var layer = new ValidateToken<AdminCreateUserRequest, AdminCreateUserResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(req -> {
                    var user = UserEntity.of(req.getPayload());
                    var createdUser = UserController.getInstance().createUser(user);
                    return AdminCreateUserResponse.of(createdUser);
                });
        return layer.next(request);
    }
}
