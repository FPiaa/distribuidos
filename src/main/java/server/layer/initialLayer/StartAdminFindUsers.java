package server.layer.initialLayer;

import protocol.request.AdminFindUsersRequest;
import protocol.response.FindUsersResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.dto.UserDTO;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

import java.util.List;

public class StartAdminFindUsers extends StartTemplate<AdminFindUsersRequest> {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        AdminFindUsersRequest request = buildRequest(jsonString, AdminFindUsersRequest.class);

        Layer<AdminFindUsersRequest, FindUsersResponse> validateUser = new ValidateToken<AdminFindUsersRequest, FindUsersResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(req -> {
                    var userController = UserController.getInstance();
                    List<UserDTO> users = userController.findUsers()
                            .map(UserDTO::of)
                            .toList();
                    return new FindUsersResponse(users);
                });
        return validateUser.next(request);


    }
}
