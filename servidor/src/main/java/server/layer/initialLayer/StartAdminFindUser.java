package server.layer.initialLayer;

import protocol.request.AdminFindUserRequest;
import protocol.response.FindUserResponse;
import protocol.response.Response;
import server.controller.UserController;
import protocol.commons.dto.UserDTO;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartAdminFindUser extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        AdminFindUserRequest request = buildRequest(jsonString, AdminFindUserRequest.class);

        var layer = new ValidateToken<AdminFindUserRequest, FindUserResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService((req) -> {
                    UserController controller = UserController.getInstance();
                    UserDTO user = controller.findUser(req.getPayload().registro());
                    return new FindUserResponse(user);
                });

        return layer.next(request);
    }
}
