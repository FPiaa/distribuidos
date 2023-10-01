package server.layer.finishLayer;

import jwt.JwtHelper;
import protocol.request.LoginRequest;
import protocol.response.LoginResponse;

public class ProcessLogin extends FinishLayerTemplate<LoginRequest, LoginResponse> {

    @Override
    public LoginResponse finish(LoginRequest requisition) {
        // TODO: search for user
        final String token = JwtHelper.createJWT(false, 1);
        return new LoginResponse(token);
    }
}
