package server.layer.initialLayer;

import protocol.request.AdminUpdateUserRequest;
import protocol.response.UpdateUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.dto.UpdateUser;
import server.dto.UserDTO;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartAdminUpdateUser extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, AdminUpdateUserRequest.class);

        var layer = new ValidateToken<AdminUpdateUserRequest, UpdateUserResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService((req) -> {
                    UserController controller = UserController.getInstance();
                    var payload = req.getPayload();
                    var user = UpdateUser.builder()
                            .senha(payload.senha())
                            .email(payload.email())
                            .nome(payload.nome())
                            .tipo(payload.tipo())
                            .registro(payload.registro())
                            .build();

                    UserDTO updatedUser = controller.updateUser(user);
                    return new UpdateUserResponse(updatedUser);
                });

        return layer.next(request);
    }
}
