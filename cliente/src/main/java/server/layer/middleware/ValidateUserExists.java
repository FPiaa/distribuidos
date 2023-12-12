package server.layer.middleware;

import jwt.JwtHelper;
import protocol.request.Request;
import protocol.response.Response;
import server.controller.UserController;
import server.exceptions.InvalidTokenException;
import server.exceptions.ResourceNotFoundException;
import server.exceptions.ServerResponseException;

public class ValidateUserExists<Req extends Request<?>, Res extends Response<?>> extends LayerTemplate<Req, Res> {
    @Override
    public void check(Req request) throws ServerResponseException {
        long id = JwtHelper.getId(request.getHeader().token());
        try {
            UserController.getInstance().findUser(id);
        } catch (ResourceNotFoundException ignored) {
            throw new InvalidTokenException();
        }
    }
}
