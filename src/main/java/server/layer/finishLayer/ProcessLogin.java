package server.layer.finishLayer;

import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import protocol.request.LoginRequest;
import protocol.response.LoginResponse;
import server.controller.UserController;
import server.exceptions.BadRequestException;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.FinishLayer;

public class ProcessLogin implements FinishLayer<LoginRequest, LoginResponse> {

    @Override
    public LoginResponse finish(LoginRequest requisition) throws ServerResponseException {
        try {
            ValidationHelper.validate(requisition.payload());
        } catch (ConstraintViolated e) {
            throw new BadRequestException(e.getMessage());
        }

        String token = UserController.getInstance().login(requisition.payload());
        return new LoginResponse(token);
    }
}
