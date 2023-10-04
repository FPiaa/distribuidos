package server.layer.finishLayer;

import protocol.request.FindUsersRequest;
import protocol.response.FindUsersResponse;
import server.controller.UserController;
import server.entity.User;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.FinishLayer;

import java.util.List;

public class ProcessFindUsers implements FinishLayer<FindUsersRequest, FindUsersResponse> {
    public FindUsersResponse finish(FindUsersRequest request) throws ServerResponseException {
        List<User> users = UserController.getInstance().findUsers();
        return new FindUsersResponse(users);
    }
}
