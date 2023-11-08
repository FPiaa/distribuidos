package server.layer.initialLayer;

import jwt.JwtHelper;
import protocol.request.DeleteUserRequest;
import protocol.request.LoginRequest;
import protocol.response.DeleteUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.dto.DeleteUser;
import server.exceptions.ForbiddenAccessException;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateToken;

public class StartDeleteUser extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, DeleteUserRequest.class);

        var layer = new ValidateToken<DeleteUserRequest, DeleteUserResponse>()
                .buildService(req -> {
                    UserController controller = UserController.getInstance();
                    LoginRequest.Payload loginValidation = new LoginRequest.Payload(req.getPayload().email(), req.getPayload().password());
                    String validationToken = controller.login(loginValidation);

                    long requestUserId = JwtHelper.getId(req.getHeader().token()) ;
                    long loginUserId = JwtHelper.getId(validationToken);

                    if(requestUserId != loginUserId) {
                        throw new ForbiddenAccessException();
                    }


                    String token = req.getHeader().token();
                    var deleteUser = new DeleteUser(requestUserId,
                            JwtHelper.getAdminStatus(token),
                            requestUserId);

                    controller.deleteUser(deleteUser);
                    return new DeleteUserResponse();
                });

        return layer.next(request);
    }
}
