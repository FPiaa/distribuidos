package server.layer.initialLayer;

import protocol.request.AdminCreateUserRequest;
import protocol.response.AdminCreateUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.dto.CreateUser;
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
                    var payload = req.getPayload();
                    var user = CreateUser.builder()
                            .tipo(payload.tipo())
                            .nome(payload.nome())
                            .senha(payload.senha())
                            .email(payload.email())
                            .build();
                    var createdUser = UserController.getInstance().createUser(user);
                    return new AdminCreateUserResponse(createdUser);
                });
        return layer.next(request);
    }
}
