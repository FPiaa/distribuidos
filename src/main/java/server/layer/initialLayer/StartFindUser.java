package server.layer.initialLayer;

import jwt.JwtHelper;
import protocol.request.FindUserRequest;
import protocol.response.FindUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateToken;

public class StartFindUser extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, FindUserRequest.class);

        Layer<FindUserRequest, FindUserResponse> initial = new ValidateToken<FindUserRequest, FindUserResponse>()
                .buildService(req -> {
                    long userId = JwtHelper.getId(req.getHeader().token());
                    var user = UserController.getInstance().findUser(userId);

                    return new FindUserResponse(user);
                });

        return initial.next(request);
    }
}
