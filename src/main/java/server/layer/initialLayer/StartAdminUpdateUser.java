package server.layer.initialLayer;

import jwt.JwtHelper;
import protocol.request.AdminUpdateUserRequest;
import protocol.response.Response;
import protocol.response.UpdateUserResponse;
import server.controller.UserController;
import protocol.commons.dto.UpdateUser;
import protocol.commons.dto.UserDTO;
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
                            .senderTipo(JwtHelper.getAdminStatus(req.getHeader().token()))
                            .registro(payload.registro())
                            .registroSender(JwtHelper.getId(req.getHeader().token()))
                            .build();

                    UserDTO updatedUser = controller.updateUser(user);
                    return new UpdateUserResponse(updatedUser);
                });

        return layer.next(request);
    }
}
