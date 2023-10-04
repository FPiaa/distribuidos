package server.layer.finishLayer;

import protocol.request.FindUserRequest;
import protocol.response.FindUserResponse;
import server.controller.UserController;
import server.entity.User;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.FinishLayer;

public class ProcessFindUser implements FinishLayer<FindUserRequest, FindUserResponse> {
    @Override
    public FindUserResponse finish(FindUserRequest request) throws ServerResponseException {
        User user = UserController.getInstance().findUser(request.payload().registro());
        return new FindUserResponse(user);
    }
}
