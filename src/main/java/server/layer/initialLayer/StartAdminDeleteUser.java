package server.layer.initialLayer;

import jwt.JwtHelper;
import protocol.request.AdminDeleteUserRequest;
import protocol.response.AdminDeleteUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.dto.DeleteUser;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartAdminDeleteUser extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, AdminDeleteUserRequest.class);
        var layer = new ValidateToken<AdminDeleteUserRequest, AdminDeleteUserResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(req -> {
                    var payload = req.getPayload();
                    int idSender = JwtHelper.getId(req.getHeader().token());
                    boolean isAdmin = JwtHelper.getAdminStatus(req.getHeader().token());
                    var user = DeleteUser.builder()
                            .registroSender(idSender)
                            .registroToDelete(payload.registro())
                            .isSenderAdmin(isAdmin)
                            .build();
                    UserController.getInstance().deleteUser(user);
                    return new AdminDeleteUserResponse();
                });
        return layer.next(request);
    }
}
