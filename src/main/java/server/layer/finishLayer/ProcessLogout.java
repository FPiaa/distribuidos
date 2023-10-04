package server.layer.finishLayer;

import protocol.request.LogoutRequest;
import protocol.response.LogoutResponse;
import server.layer.interfaces.FinishLayer;

public class ProcessLogout implements FinishLayer<LogoutRequest, LogoutResponse> {

    @Override
    public LogoutResponse finish(LogoutRequest request) {
        return new LogoutResponse();
    }
}
