package server.layer.interfaces;

import protocol.response.Response;

public interface InitialLayer {
    Response<?> startService(String jsonString);
}
