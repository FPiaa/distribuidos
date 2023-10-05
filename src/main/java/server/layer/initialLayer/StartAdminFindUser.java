package server.layer.initialLayer;

import protocol.request.FindUserRequest;
import protocol.response.FindUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.entity.User;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateUser;

public class StartAdminFindUser extends StartTemplate<FindUserRequest>{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        FindUserRequest request = buildRequest(jsonString, FindUserRequest.class);

        Layer<FindUserRequest, FindUserResponse> layer = new ValidateUser<FindUserRequest, FindUserResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService((req) -> {
                    UserController controller = UserController.getInstance();
                    User user = controller.findUser(req.getPayload().registro());
                    return new FindUserResponse(user);
                });

        return layer.next(request);
    }
}
