package server.layer.finishLayer;

import request.LogoutRequest;
import response.LogoutResponse;
import response.Response;
import server.layer.interfaces.FinishLayer;
import server.layer.interfaces.Layer;

public class ProcessLogout implements FinishLayer<LogoutRequest, LogoutResponse> {

    @Override
    public LogoutResponse finish(LogoutRequest request) {
        return new LogoutResponse();
    }

    @Override
    public boolean check(LogoutRequest request) {
        return false;
    }

    @Override
    public Response<?> next(LogoutRequest request) {
        return null;
    }

    @Override
    public Layer<LogoutRequest, LogoutResponse> addLayer(Layer<LogoutRequest, LogoutResponse> newLayer) {
        return null;
    }

    @Override
    public void buildService(FinishLayer<LogoutRequest, LogoutResponse> consumer) {
    }
}
