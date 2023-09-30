package server.layer.interfaces;

import response.Response;

public interface InitialLayer {
    Response<?> startService(String jsonString);
}
