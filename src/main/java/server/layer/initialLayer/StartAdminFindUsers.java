package server.layer.initialLayer;

import protocol.request.AdminFindUsersRequest;
import protocol.response.FindUsersResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.dto.UserDTO;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

import java.util.List;

public class StartAdminFindUsers extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        AdminFindUsersRequest request = buildRequest(jsonString, AdminFindUsersRequest.class);

        var validateUser = new ValidateToken<AdminFindUsersRequest, FindUsersResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(req -> {
                    var userController = UserController.getInstance();
                    List<UserDTO> users = userController.findUsers();
                    return new FindUsersResponse(users);
                });
        return validateUser.next(request);


    }
}
