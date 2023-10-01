package server.layer.finishLayer;

import protocol.request.LogoutRequest;
import protocol.response.LogoutResponse;

public class ProcessLogout extends FinishLayerTemplate<LogoutRequest, LogoutResponse> {

    @Override
    public LogoutResponse finish(LogoutRequest request) {
        return new LogoutResponse();
    }
}
