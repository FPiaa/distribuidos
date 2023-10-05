package server.layer.initialLayer;

import protocol.request.AdminFindUsersRequest;
import protocol.response.FindUsersResponse;
import protocol.response.Response;
import server.controller.UserController;
import server.entity.User;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateUser;

import java.util.List;

public class StartAdminFindUsers extends StartTemplate<AdminFindUsersRequest>{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        AdminFindUsersRequest request = buildRequest(jsonString, AdminFindUsersRequest.class);

        Layer<AdminFindUsersRequest, FindUsersResponse> validateUser = new ValidateUser<AdminFindUsersRequest, FindUsersResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(req -> {
                    var userController = UserController.getInstance();
                    List<User> users = userController.findUsers();
                    return new FindUsersResponse(users);
                });
        return validateUser.next(request);


    }
}
