package server.layer.finishLayer;

import jwt.JwtHelper;
import protocol.request.LoginRequest;
import protocol.response.LoginResponse;
import protocol.response.Response;
import server.layer.interfaces.FinishLayer;
import server.layer.interfaces.Layer;

public class ProcessLogin implements FinishLayer<LoginRequest, LoginResponse> {

    @Override
    public LoginResponse finish(LoginRequest requisition) {
        // TODO: search for user
        final String token = JwtHelper.createJWT(false, 1);
        return new LoginResponse(token);
    }

    @Override
    public boolean check(LoginRequest request) {
        return false;
    }

    @Override
    public Response<?> next(LoginRequest request) {
        return null;
    }

    @Override
    public Layer<LoginRequest, LoginResponse> addLayer(Layer<LoginRequest, LoginResponse> newLayer) {
        return null;
    }

    @Override
    public void buildService(FinishLayer<LoginRequest, LoginResponse> consumer) {

    }
}
