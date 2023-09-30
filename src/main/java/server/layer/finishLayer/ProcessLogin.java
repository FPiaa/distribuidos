package server.layer.finishLayer;

import jwt.JwtHelper;
import request.requisition.LoginRequest;
import response.LoginResponse;
import response.Response;
import server.interfaces.FinishLayer;
import server.interfaces.Layer;

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
