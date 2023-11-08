package server.layer.initialLayer;

import protocol.request.UpdateUserRequest;
import protocol.response.Response;
import protocol.response.UpdateUserResponse;
import server.controller.UserController;
import server.dto.UpdateUser;
import server.dto.UserDTO;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateToken;

public class StartUpdateUser extends StartTemplate {

    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, UpdateUserRequest.class);

        var layer = new ValidateToken<UpdateUserRequest, UpdateUserResponse>()
                .buildService(req -> {
                    var payload = req.getPayload();
                    var user = UpdateUser.builder()
                            .senha(payload.senha())
                            .email(payload.email())
                            .nome(payload.nome())
                            .tipo(payload.tipo())
                            .registro(payload.registro())
                            .build();

                    UserDTO updatedUser = UserController.getInstance().updateUser(user);
                    return new UpdateUserResponse(updatedUser);
                });

        return layer.next(request);
    }
}
