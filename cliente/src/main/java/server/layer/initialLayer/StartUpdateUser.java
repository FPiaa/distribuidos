package server.layer.initialLayer;

import jwt.JwtHelper;
import protocol.request.UpdateUserRequest;
import protocol.response.Response;
import protocol.response.UpdateUserResponse;
import server.controller.UserController;
import protocol.commons.dto.UpdateUser;
import protocol.commons.dto.UserDTO;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateToken;

public class StartUpdateUser extends StartTemplate {

    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, UpdateUserRequest.class);

        var layer = new ValidateToken<UpdateUserRequest, UpdateUserResponse>()
                .buildService(req -> {
                    var payload = req.getPayload();
                    long id = JwtHelper.getId(req.getHeader().token());

                    var user = UpdateUser.builder()
                            .senha(payload.senha())
                            .email(payload.email())
                            .nome(payload.nome())
                            .tipo(JwtHelper.getAdminStatus(req.getHeader().token()))
                            .registro(JwtHelper.getId(req.getHeader().token()))
                            .registroSender(JwtHelper.getId(req.getHeader().token()))
                            .senderTipo(JwtHelper.getAdminStatus(req.getHeader().token()))
                            .build();

                    UserDTO updatedUser = UserController.getInstance().updateUser(user);
                    return new UpdateUserResponse(updatedUser);
                });

        return layer.next(request);
    }
}
